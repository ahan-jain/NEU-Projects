#lang htdp/isl+

(: op? (Any -> Boolean))
; determines whether argument is the symbol of an arithmetic operator
(define (op? s)
  (member s '(+ - * /)))

(define Op (signature (predicate op?)))
 
(define-struct e-op [op left right])
(define-struct e-in [])






(define E0 3)
(define E1 (make-e-op '+ (make-e-op '- 1 (make-e-op '+ 2 2))
                      (make-e-op '* 3 (make-e-op '/ 3 4))))
(define E2 (make-e-op '+ 1 (make-e-op '- 2 3)))
(define E3 (make-e-op '+ (make-e-in) 2))

;Exercise 1-------------------------------------------------------------------------
(define S-Exp (signature (mixed Number String Boolean Symbol (ListOf S-Exp))))

(define S0 3)
(define S1 '(+ (- 1 (+ 2 2)) (* 3 (/ 3 4))))
(define S2 '(+ 1 (- 2 3)))
(define S3 '(+ (input) 2))

(: parse (S-Exp -> Exp))
; converts an S-Exp to an Exp
(define (parse sexp)
  (cond [(number? sexp) sexp] ; numbers are left alone
        [(string? sexp) (error "Invalid String")] ; strings not allowed
        [(boolean? sexp) (error "Invalid Boolean")] ; booleans not allowed
        [(symbol? sexp) (error "Invalid Symbol")] ; you should not have a symbol by itself
        [(list? sexp) (cond [(= (length sexp) 3) (if (and (op? (first sexp))
                                  (or (number? (second sexp))
                                      (e-op? (parse (second sexp)))
                                      (e-in? (parse (second sexp)))
                                      (e-mc? (parse (second sexp)))
                                      (e-mr? (parse (second sexp)))
                                      (e-m+? (parse (second sexp))))
                                  (or (number? (third sexp))
                                      (e-op? (parse (third sexp)))
                                      (e-in? (parse (third sexp)))
                                      (e-mc? (parse (third sexp)))
                                      (e-mr? (parse (third sexp)))
                                      (e-m+? (parse (third sexp)))))
                             (make-e-op (first sexp)
                                        (parse (second sexp))
                                        (parse (third sexp))) (error "Invalid List"))]
                            [(= (length sexp) 2)
                             (if (and
                                  (and (symbol? (first sexp)) (symbol=? (first sexp) 'm+))
                                  (or (number? (second sexp))
                                      (e-op? (parse (second sexp)))
                                      (e-in? (parse (second sexp)))
                                      (e-mc? (parse (second sexp)))
                                      (e-mr? (parse (second sexp)))
                                      (e-m+? (parse (second sexp)))))
                                 (make-e-m+ (parse (second sexp)))
                                 (error "Invalid List"))]
                            [(= (length sexp) 1)
                             (cond
                               [(and (symbol? (first sexp)) (symbol=? (first sexp) 'input)) (make-e-in)]
                               [(and (symbol? (first sexp)) (symbol=? (first sexp) 'mc)) (make-e-mc)]
                               [(and (symbol? (first sexp)) (symbol=? (first sexp) 'mr)) (make-e-mr)]
                               [else (error "Invalid List")])]
                                                    
                            [else (error "Invalid List")])]
        [else (error "Invalid S-Expression")]))

(check-expect (parse S0) E0)
(check-expect (parse S1) E1)
(check-expect (parse S2) E2)
(check-expect (parse S3) E3)
(check-error (parse #t))
(check-error (parse 'mikewazowski))
(check-error (parse '(+ 1 hello)))
(check-expect (parse '(- 1 2)) (make-e-op '- 1 2))
(check-expect (parse '(* (input) (input))) (make-e-op '* (make-e-in) (make-e-in)))
(check-error (parse '("hello")))
(check-error (parse '(output)))
(check-error (parse '(/ 1 2 3)))
(check-error (parse '(2 + 3)))
(check-error (parse '(2 8)))

(check-expect (parse '(+ (m+ 5) (+ (mc) (mr)))) (make-e-op '+ (make-e-m+ 5) (make-e-op '+ (make-e-mc) (make-e-mr))))
(check-expect (parse '(+ (m+ (+ 2 3)) (+ (mc) (mr)))) (make-e-op '+ (make-e-m+ (make-e-op '+ 2 3)) (make-e-op '+ (make-e-mc) (make-e-mr))))
(check-expect (parse '(+ (m+ (input)) (+ (mc) (mr)))) (make-e-op '+ (make-e-m+ (make-e-in)) (make-e-op '+ (make-e-mc) (make-e-mr))))
(check-error (parse '(+ (m+ (+ 2 3)) (+ (mc 5) (mr))))) 
(check-expect (parse '(mr)) (make-e-mr))
(check-expect (parse '(mc)) (make-e-mc))
(check-error (parse '(m+)))
(check-error (parse '(+ 1 (m+))))
(check-error (parse '(+ 1 (m+ 1) (mc) (mc))))
(check-error (parse '(m+ "yo")))




;Exercise 2-------------------------------------------------------------------------
(: eval (Exp -> Result))
; evaluates a exp by sending it to eval/aux with a memory cell holding the default value 0
(define (eval exp)
  ((eval/aux exp) (lambda (x y) x) 0))

(check-expect (eval E0)  3)
(check-expect (eval E1)  -0.75)
(check-expect (eval E2)  0)
(check-expect (resume (eval E3) 5) 7)
(check-expect (resume (eval (parse '(input))) 5) 5)
(check-expect (resume (resume (eval (parse '(+ (input) (input)))) 5) 10) 15)
(check-expect (eval (parse '(+ (m+ 5) (+ (m+ 2) (mr))))) 7)
(check-expect (eval (parse '(+ (m+ -5) (+ (m+ 1) (mr))))) -4)
(check-expect (eval (parse '(+ (mr) (+ (mr) (mr))))) 0)
(check-expect (eval (parse '(+ (m+ -20) (+ (mc) (mr))))) 0)
(check-expect (eval (parse `(+ (m+ 432) (+ (+ (mc) (m+ 6)) (mr))))) 6)
(check-expect (eval (parse `(+ (m+ 6) 5))) 5)
(check-expect (eval (parse `(+ (m+ 6) (* 8 (mr))))) 48)
(check-expect (eval (parse `(+ (mc) (+ (m+ 2) (mr))))) 2)

;Exercise 3-------------------------------------------------------------------------
(: simplify (Exp -> Exp))
;;simplify takes in an Expression and returns a simplified expression based on
;specific occurrences
(define (simplify-exp op)
  (make-e-op (e-op-op op)
             (simplify (e-op-left op))
             (simplify (e-op-right op))))

(define (add exp)
  ; simplifies two expressions that are being added together (if either one of them is 0 it returns the other)
  (cond 
    [(and (number? (e-op-left exp)) (= (e-op-left exp) 0)) (simplify (e-op-right exp))]
    [(and (number? (e-op-right exp)) (= (e-op-right exp) 0)) (simplify (e-op-left exp))]
    [else (simplify-exp exp)]))

(check-expect (add (parse '(+ 1 0))) 1)
(check-expect (add (parse '(+ 0 1))) 1)
(check-expect (add (parse '(+ (+ 1 0) 0))) 1)


(define (mult exp)
  ; simplifies two expressions that are being multiplied together (if either one of them is 1 it returns the other)
  (cond
    [(and (number? (e-op-left exp)) (= (e-op-left exp) 1)) (simplify (e-op-right exp))]
    [(and (number? (e-op-right exp)) (= (e-op-right exp) 1)) (simplify (e-op-left exp))]
    [else (simplify-exp exp)]))

(check-expect (mult (parse '(* 3 1))) 3)
(check-expect (mult (parse '(* 1 3))) 3)
(check-expect (mult (parse '(* 1 (* 1 3)))) 3)

(define (sub exp)
  ; simplifies two expressions that are being added together (if the right one of them is 0 it returns the other)
  (cond
    [(and (number? (e-op-right exp)) (= (e-op-right exp) 0)) (simplify (e-op-left exp))]
    [else (simplify-exp exp)]))

(check-expect (sub (parse '(- 1 0))) 1)
(check-expect (sub (parse '(- (- 1 0) 0))) 1)

(define (div exp)
  ; simplifies two expressions that are being added together (if the right one of them is 1 it returns the other)
  (cond
    [(and (number? (e-op-right exp)) (= (e-op-right exp) 1)) (simplify (e-op-left exp))]
    [else (simplify-exp exp)]))

(check-expect (div (parse '(/ 3 1))) 3)
(check-expect (div (parse '(/ 3 (/ 3 1)))) (parse '(/ 3 3)))

(define (simplify exp)
  ; returns an expression that has been simplified be replacing any occurrences of
  ; (+ e 0), (+ 0 e) or (- e 0) with e and (* e 1) or (* 1 e) or (/ e 1) with e
  (cond [(number? exp) exp]
        [(e-op? exp) (cond [(symbol=? (e-op-op exp) '+) (add (simplify-exp exp))]
                           [(symbol=? (e-op-op exp) '*) (mult (simplify-exp exp))]
                           [(symbol=? (e-op-op exp) '-) (sub (simplify-exp exp))]
                           [(symbol=? (e-op-op exp) '/) (div (simplify-exp exp))])]
        [(e-in? exp) exp]))

(check-expect (simplify E0) 3)
(check-expect (simplify (parse '(+ 0 25))) 25)
(check-expect (simplify (parse '(+ (- (input) (* 25 1)) (+ 1 (- 2 3))))) (parse '(+  (- (input) 25) (+ 1 (- 2 3)))))
(check-expect (simplify (parse '(+ (* 5 1) (input)))) (parse '(+ 5 (input))))
(check-expect (simplify (parse '(- (* 13 (- 1 0)) (+ 27 0)))) (parse '(- 13 27)))
(check-expect (simplify (parse '(- 0 (/ 15 1))))(parse '(- 0 15)))
(check-expect (simplify (parse '(- (* 3 15) 8))) (parse '(- (* 3 15) 8)))
(check-expect (simplify (parse '(* 1 (- 473 0)))) 473)
(check-expect (simplify (parse '(* (-  473 0) 1))) 473)
(check-expect (simplify (parse '(* (+ 22 (+ 4 88)) (- 18 17)))) (parse '(* (+ 22 (+ 4 88)) (- 18 17))))
(check-expect (simplify (parse '(/ (input) 1))) (parse '(input)))
(check-expect (simplify (parse '(/ 1 (input)))) (parse '(/ 1 (input))))
(check-expect (simplify (parse '(/ 3 (* 5 (input))))) (parse '(/ 3 (* 5 (input)))))
(check-expect (simplify (parse '(input))) (parse '(input)))




;Exercise 4-------------------------------------------------------------------------
(: compact (Exp -> Exp))
; given a Exp, replaces all constant expressions with the number that they evaluate to
(define (compact exp)
  (cond
    [(number? exp) exp]
    [(e-op? exp)
     (if (and (number? (compact (e-op-left exp))) (number? (compact (e-op-right exp))))
         (operate (e-op-op exp) (compact (e-op-left exp)) (compact (e-op-right exp)))
         (make-e-op (e-op-op exp) (compact (e-op-left exp)) (compact (e-op-right exp))))]
    [(e-in? exp) exp]))


(: operate (Symbol Number Number -> Number))
; evaluates a exp, with the given number as the input
 (define (operate op left right)
      (cond
        [(symbol=? op '+) (+ left right)]
        [(symbol=? op '-) (- left right)]
        [(symbol=? op '*) (* left right)]
        [(symbol=? op '/) (/ left right)]))

(check-expect (compact E3) E3)
(check-expect (compact E2) 0)
(check-expect (compact (make-e-op '/ (make-e-in) (make-e-op '* 7 8))) (make-e-op '/ (make-e-in) 56))
(check-expect (compact (make-e-op '+ 2 (make-e-op '+ 3 (make-e-in)))) (make-e-op '+ 2 (make-e-op '+ 3 (make-e-in))))
(check-expect (compact (make-e-op '- (make-e-in) (make-e-op '+ 2 3))) (make-e-op '- (make-e-in) 5))
(check-expect (compact (make-e-op '* (make-e-op '/ 2 (make-e-in)) 9)) (make-e-op '* (make-e-op '/ 2 (make-e-in)) 9))

;Exercise 5-------------------------------------------------------------------------
(: run (S-Exp -> Result))
; parses, compacts, simplifies, and then evaluates the S-Expression
(define (run sexp)
  (eval (parse sexp)))

(check-expect (run S0) 3)
(check-expect (run S1) -0.75)
(check-expect (run S2) 0)
(check-expect (resume (run S3) 2) 4)
(check-expect (resume (run '(input)) 13) 13)
(check-expect (resume (resume (run '(+ (- 15 (+ (input) 400)) (* 5 (/ (input) -10)))) 22) 22) -418)
(check-expect (run '(+ (m+ 5) (+ (m+ 2) (mr)))) 7)
(check-expect (run `(+ (m+ 5) (+ (mc) (mr)))) 0)
(check-expect (run `(+ (mc) (+ (m+ 5) (mr)))) 5)
(check-expect (run `(* (+ 1 (m+ 5)) (mc))) 0)
(check-expect (run '(* (+ 7 (m+ (+ 1 (m+ 5)))) (- 7 (mr)))) 7)
(check-expect (run '(- (mc) (+ (m+ 2) (mr)))) -2)
(check-expect (resume (run '(- (m+ (input)) (mr))) 4) -4)




;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;                                                                                          ;
;------------------------------------------- 10b ------------------------------------------;
;                                                                                          ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


; an (EM+Of Exp)
; represents a program that computes its argument and adds it to the value in memory
(define-struct e-m+ [arg])
; an EMr represents a program that recalls a value from memory
(define-struct e-mr [])
; an EMc represents a program that clears the value in memory (sets it to zero)
(define-struct e-mc [])

(define Memory (signature Number))
;; because we are threading the memory's value in eval/aux in the form of a number
;; we just thought it would be more clear to make a signature for Memory to indicate what the number represents

; an Exp is a
(define Exp (signature (mixed Number
                              (EOpOf Op Exp Exp)
                              (EM+Of Exp)
                              EMr
                              EMc
                              EIn)))
; representing a calculator program that can prompt for input and manipulate state

(define-struct input [resume])
; AInput is a
(define AInput (signature [InputOf (Number -> Result)]))
; representing a suspended calculator computation awaiting user input

(define I1 (make-input (lambda (k) k)))
(define I2 (make-input (lambda (k) (* 5 k))))
(define I3 (make-input (lambda (k) (* 2 (/ k 3)))))
(define I4 (make-input (lambda (k) (make-input (lambda (input) (+ k input))))))
(define I5 (make-input (lambda (k) (+ 7 (* 2 (- (/ k 3) 6))))))


 
; a Result is a
(define Result (signature (mixed Number AInput)))
; where a Number is a final answer and a AInput is awaiting user input

(define R1 5)
(define R2 115)
(define R3 20)
(define R4 (make-input (lambda (input) (+ 5 input)))) ; this represents what should be the result after calling resume on I4
(define R5 11) ; this represents what should be the result after passing the number 6 onto R4
(define R6 7)

(: resume (AInput Number -> Result))
; continues the computation with the given input.
(define (resume input-func num)
    ((lambda (k) ((input-resume input-func) num)) identity))

(check-expect (resume I1 5) R1)
(check-expect (resume I2 23) R2)
(check-expect (resume I3 30) R3)
(check-satisfied (resume I4 5) input?) ; this indicates that after inputting one value, it still needs another value to evaluate to a Number as there were two inputs in I4 (i.e. it currently is AInput)
(check-expect (resume (resume I4 5) 6) R5); this evaluates to 11 after both inputs 5 and 6, as desired.
(check-expect (resume I5 18) R6)


; A Cont is a
(define Cont (signature (Number Memory -> Result)))
; where the Number is the answer of the previous computation




#|
Here is an example program to demonstrate the difference between left-to-right evaluation vs right-to-left evaluation:

(resume (resume (eval (parse '(+ (- 15 (+ (input) 400)) (* 5 (/ (input) -10))))) 22) 25)

Our expression is (+ (- 15 (+ (input) 400)) (* 5 (/ (input) -10))) 
Our two inputs are 22 and 25.

------------------------------------------------------------------------------------------------
LEFT-TO-RIGHT EVALUATION:

Evaluating left to right, the new expression would be: (+ (- 15 (+ 22 400)) (* 5 (/ 25 -10)))
This is because the left subexpression, which is being evaluated first, gets 22 and the right subexpression, evaluated after the left, gets 25.

- the left subexpression would be (- 15 (+ 22 400)), which is equal to -407 
- the right subexpression would be (* 5 (/ 25 -10)), which is equal to -12.5 

 this would be equal to (+ (-407) (-12.5)), which is equal to -419.5
------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------

RIGHT-TO-LEFT EVALUATION:

Evaluating right to left, the new expression would be: (+ (- 15 (+ 25 400)) (* 5 (/ 22 -10)))
This is because the right subexpression, which is being evaluated first, gets 22 and the left subexpression, evaluated after the right, gets 25.

- the right subexpression would be (* 5 (/ 22 -10)), which is equal to -11
- the left subexpression would be (- 15 (+ 25 400)), which is equal to -410

 this would be equal to (+ (-11) (-410)), which is equal to -421
------------------------------------------------------------------------------------------------
|#



(: eval/aux (Exp -> (Cont Memory -> Result)))
; evaluates expressions left-to-right using CPS
(define (eval/aux exp)
  (local [(define (oper op) ; returns the operation based on the symbol given
            (cond
              [(symbol=? op '+) +]
              [(symbol=? op '-) -]
              [(symbol=? op '*) *]
              [(symbol=? op '/) /]))]
    (cond 
      [(number? exp) (lambda (k m) (k exp m))] ; if its a number, it returns a continuation with the number itself and the memory intact as we haven't yet evaluated anything further yet
      [(e-op? exp)  (lambda (k m)
                      ((eval/aux (e-op-left exp))
                       (lambda (n1 m2) ; stores the value of the left subexpression in n1 and stores the memory of the expression (if any) in a memory space m2
                         ((eval/aux (e-op-right exp))
                          (lambda (n2 m3)  ; stores the value of the right subexpression in n2 and stores the memory of the expression (if any) in a memory space m3
                            (k ((oper (e-op-op exp)) n1 n2) m3))
                          m2)) 
                       m))]   ; returns a continuation with the evaluated expression and the memory value of the expression
      [(e-in? exp) (lambda (k m) (make-input (lambda (input) (k input m))))] ; returns a continuation with the number that the user would input (using resume) and the memory intact as we haven't yet evaluated anything further yet
      [(e-m+? exp) (lambda (k m) ((eval/aux (e-m+-arg exp))
                                  (lambda (n1 m2)
                                    (k 0 (+ m n1 m2))) 0))] ; returns a continuation with the memory cell's value added to the total value that was present in m+ beforehand, and returns the number 0
      [(e-mr? exp) (lambda (k m) (k m m))]; returns a continuation with the number stored in the memory (as it is being recalled), and keeps the memory itself intact as we haven't yet evaluated anything further yet 
      [(e-mc? exp) (lambda (k m) 
                     (k 0 0))]))) ; returns a continuation with the number 0 and the memory value 0, as the value in memory has been cleared.

(check-expect ((eval/aux E0) (lambda (x y) x) 0)  3)
(check-expect ((eval/aux E1) (lambda (x y) x) 0)  -0.75)
(check-expect ((eval/aux E2) (lambda (x y) x) 0)  0)
(check-expect (resume ((eval/aux E3) (lambda (x y) x) 0) 5) 7)
(check-expect (resume ((eval/aux (parse '(input))) (lambda (x y) x) 0) 5) 5)
(check-expect (resume (resume ((eval/aux (parse '(+ (input) (input)))) (lambda (x y) x) 0) 5) 10) 15)
(check-expect ((eval/aux (parse `(+ (m+ 5) (+ (m+ 2) (mr))))) (lambda (x y) x) 0) 7)
(check-expect ((eval/aux (parse `(+ (mr) (+ (mr) (mr))))) (lambda (x y) x) 0) 0)
(check-expect ((eval/aux (parse `(+ (m+ -20) (+ (mc) (mr))))) (lambda (x y) x) 0) 0)
(check-expect ((eval/aux (parse `(+ (m+ 432) (+ (+ (mc) (m+ 6)) (mr))))) (lambda (x y) x) 0) 6)
(check-expect ((eval/aux (parse `(+ (m+ 6) 5))) (lambda (x y) x) 0) 5)
(check-expect ((eval/aux (parse `(+ (m+ 6) (* 8 (mr))))) (lambda (x y) x) 0) 48)
(check-expect ((eval/aux (parse `(+ (mc) (+ (m+ 2) (mr))))) (lambda (x y) x) 0) 2)





