#lang htdp/isl+

(require "lib.rkt")

(define-struct e-num [num])
(define ANumber (signature [ENumOf Number]))

(define-struct e-var [sym])
(define ASymbol (signature [EVarOf Symbol]))


(define-struct e-op [op left right])
(define AnOperation (signature [EOpOf Symbol ExprV1 ExprV1]))


(define-struct e-lam [param body])
(define ALambda (signature [ELamOf Symbol ExprV1]))


(define-struct e-app [fun arg])
(define AppFunc (signature [EAppOf ExprV1 ExprV1]))

(define ExprV1 (signature (mixed (ENumOf Number)
                                 (EVarOf Symbol)
                                 (EOpOf Op ExprV1 ExprV1)
                                 (ELamOf Symbol ExprV1)
                                 (EAppOf ExprV1 ExprV1))))

(define ValV1 (signature (mixed (ENumOf Number) (ELamOf Symbol ExprV1))))


;;EXERCISE 1--------------------------------------------------------------------------------------

(: op? (Any -> Boolean))
; determines whether argument is the symbol of an arithmetic operator
(define (op? s)
  (member s '(+ - * /)))

(check-expect (op? '+) #t)
(check-expect (op? '-) #t)
(check-expect (op? '*) #t)
(check-expect (op? '/) #t)
(check-expect (op? 'hi) #f)
(check-expect (op? 'subtract) #f)

(define Op (signature (predicate op?)))

; An Operation is a (make-e-op Op Expr Expr)
; Interpretation: performing an arithmetic operation on two expressions in a calculator
; where each expression is any one of the types listed under an Expr
;(define-struct oper [op left right])
;(define AnOperation (signature [OperOf Op Expr Expr]))

(define O1 (make-e-op '+ (make-e-num 4) (make-e-num 5)))
(define O2 (make-e-op  '-(make-e-num 10) (make-e-num 3)))
(define O3 (make-e-op '* (make-e-num 9) (make-e-num 23)))
(define O4 (make-e-op  '/ (make-e-num 18) (make-e-num 3)))
(define O5 (make-e-op  '+ (make-e-op  '- (make-e-num 9) (make-e-num 3)) (make-e-num 5)))
(define O6 (make-e-op  '- (make-e-num 50) (make-e-op  '* (make-e-num 4) (make-e-num 1))))
(define O7 (make-e-op '+ (make-e-op  '- (make-e-num 91) (make-e-num 90)) (make-e-op  '/ (make-e-num 69) (make-e-num 23))))

(define (e-op-temp expr)
  (... (e-op-op expr) ... (expr-temp (e-op-left expr)) ... (expr-temp (e-op-left expr))...))

; An Operation is a (make-e-lam Op Symbol Expr)
; Interpretation: a lambda function in the form of a struct where var is the
;; argument of the lambda and an Expr is the expression of the lambda 
;(define-struct e-lam [var exp])
;(define ALambda (signature [MklambdaOf Symbol Expr]))

(define L1 (make-e-lam 'x (make-e-var 'x)))
(define L2 (make-e-lam 'x (make-e-op  '+ (make-e-num 4) (make-e-var 'x))))
(define L3 (make-e-lam 'x (make-e-op  '* (make-e-num 2) (make-e-var 'x))))
(define L4 (make-e-lam 'x (make-e-op  '/ (make-e-var 'x) (make-e-num 3))))
(define L5 (make-e-lam 'x (make-e-lam 'y (make-e-op  '+ (make-e-var 'x) (make-e-var 'y)))))

(define (e-lam-temp expr)
 (... (e-lam-param expr) ... (expr-temp (e-lam-body expr))...))


; An e-app is a (make-e-app Expr Expr)
; Interpretation: a function application where the func is an Expr (although it should only work for an Mklambda or e-app)
;; and the val is the applied value, which is also an Expr
;(define-struct e-app [func val])
;(define AnApp (signature [AppfuncOf Expr Expr]))


(define A1 (make-e-app L1 (make-e-num 5)))
(define A2 (make-e-app L2 (make-e-num 3)))
(define A3 (make-e-app L3 (make-e-app L1 (make-e-num 29))))
(define A4 (make-e-app L4 O2))
(define A5 (make-e-app L5 O5))

(define (e-app-temp expr)
   (... (expr-temp (e-app-fun)) ... (expr-temp (e-app-arg)) ...))




(define E1 (make-e-num 10))
(define E2 (make-e-op '+ (make-e-num 1) (make-e-num 2)))
(define E3 (make-e-op '* (make-e-op  '- (make-e-num 1)(make-e-num 2)) (make-e-op  '/ (make-e-num 3) (make-e-num 4))))
(define E4 (make-e-lam 'x (make-e-op  '+ (make-e-var 'x) (make-e-num 1))))
(define E5 (make-e-app (make-e-lam 'x (make-e-var 'x)) (make-e-num 10)))


(define (expr-temp expr)
  (cond
    [(e-num? expr)...]
    [(e-var? expr)...]
    [(e-lam? expr) (e-lam-temp expr)]
    [(e-op? expr) (e-op-temp expr)]
    [(e-app? expr) (e-app-temp expr)]))
                          
                                       


;;EXERCISE 2--------------------------------------------------------------------------------------

(define S-Exp (signature (mixed Number String Boolean Symbol (ListOf S-Exp))))

(define S1 10)
(define S2 '(+ 1 2))
(define S3 '(* (- 1 2) (/ 3 4)))
(define S4 '(lambda (x) (+ x 1)))
(define S5 '((lambda (x) x) 10))

;;;;;;;;;;;;;;;;;;;;;;

;;   V1 Functions   ;;

;;;;;;;;;;;;;;;;;;;;;;

(: parse-v1 (S-Exp -> ExprV1))
; parses an S-Exp, out of valid formats include:
; - a number
; - a symbol
#|
- a list, and a list can be:
  - (list an-operation Expr Expr), where an-operation is a symbol that can be '+ '- '* or '/
  - (list 'lambda lambda-argument Expr)
  - (list (list 'lambda lambda-argument Expr) input-value)
|#

(define (parse-v1 sexp)
  (cond [(number? sexp) (make-e-num sexp)] ; numbers are left alone
        [(string? sexp) (error "Invalid String")] ; strings not allowed
        [(boolean? sexp) (error "Invalid Boolean")] ; booleans not allowed
        [(symbol? sexp) (make-e-var sexp)] ; symbols are left alone
        [(list? sexp) (cond [(= (length sexp) 3)
                             (cond
                               ; Checking whether its an arithmetic expression
                               [(op? (first sexp))  (make-e-op (first sexp)
                                                               (parse-v1 (second sexp))
                                                               (parse-v1 (third sexp)))]
                               
                               ; Checking whether its a one argument lambda
                               [(and (and (symbol? (first sexp)) (symbol=? 'lambda (first sexp)))
                                     (and (list? (second sexp))
                                          (= (length (second sexp)) 1)
                                          (symbol? (first (second sexp)))))
                                (make-e-lam (first (second sexp))
                                               (parse-v1 (third sexp)))]
                               [else (error "Invalid List")])]
                            
                            [(= (length sexp) 2)
                             ; Checking whether its a function application
                             (make-e-app (parse-v1 (first sexp))
                                           (parse-v1 (second sexp)))]
                             
                            [else (error "Invalid List")])]
                             
                            
        
        [else (error "Invalid S-Expression")])) ; any other input is an invalid S-Expression by definition
                                       

(check-expect (parse-v1 S1) E1)
(check-expect (parse-v1 S2) E2)
(check-expect (parse-v1 S3) E3)
(check-expect (parse-v1 S4) E4)
(check-expect (parse-v1 S5) E5)
(check-expect (parse-v1 '(+ 4 5)) O1)
(check-expect (parse-v1 '(- 10 3)) O2)
(check-expect (parse-v1 '(* 9 23)) O3)
(check-expect (parse-v1 '(/ 18 3)) O4)
(check-expect (parse-v1 '(+ (- 9 3) 5)) O5)
(check-expect (parse-v1 '(- 50 (* 4 1))) O6)
(check-expect (parse-v1 '(+ (- 91 90) (/ 69 23))) O7)
(check-expect (parse-v1 '(lambda (x) x)) L1)
(check-expect (parse-v1 '(lambda (x) (+ 4 x))) L2)
(check-expect (parse-v1 '(lambda (x) (* 2 x))) L3)
(check-expect (parse-v1 '(lambda (x) (/ x 3))) L4)
(check-expect (parse-v1 '(lambda (x) (lambda (y) (+ x y)))) L5)
(check-expect (parse-v1 '((lambda (x) x) 5)) A1)
(check-expect (parse-v1 '((lambda (x) (+ 4 x)) 3)) A2)
(check-expect (parse-v1 '((lambda (x) (* 2 x)) ((lambda (x) x) 29))) A3)
(check-expect (parse-v1 '((lambda (x) (/ x 3)) (- 10 3))) A4)
(check-expect (parse-v1 '((lambda (x) (lambda (y) (+ x y))) (+ (- 9 3) 5))) A5)
(check-expect (parse-v1 '((lambda (x) x) (+ 1 0))) (make-e-app (make-e-lam 'x (make-e-var 'x)) (make-e-op  '+ (make-e-num 1) (make-e-num 0))))
(check-expect (parse-v1 '(lambda (e) y)) (make-e-lam 'e (make-e-var 'y)))
(check-expect (parse-v1 '(+ (lambda (x) x) 3)) (make-e-op  '+ (make-e-lam 'x (make-e-var 'x)) (make-e-num 3)))
(check-expect (parse-v1 '(1 2)) (make-e-app (make-e-num 1) (make-e-num 2)))
(check-error (parse-v1 #f))
(check-expect (parse-v1 'c) (make-e-var 'c))
(check-error (parse-v1 "hello"))
(check-error (parse-v1 '(lambda (v) 1 2 3)))
(check-error (parse-v1 '(lambda (x))))
(check-error (parse-v1 '((lambda (x y) y))))
(check-expect (parse-v1 '((lambda (x) x) 2)) (make-e-app (make-e-lam 'x (make-e-var 'x)) (make-e-num 2)))
(check-expect (parse-v1 '(lambda (x) (lambda (y) (lambda (z) (+ y z))))) (make-e-lam 'x (make-e-lam 'y (make-e-lam 'z (make-e-op  '+ (make-e-var 'y) (make-e-var 'z))))))
(check-error (parse-v1 '((lambda (x) x) x y z)))
(check-expect (parse-v1 '((+ 1 2) 2)) (make-e-app (make-e-op  '+ (make-e-num 1) (make-e-num 2)) (make-e-num 2)))
(check-error (parse-v1 '((lambda (x) x) 2 1)))
(check-expect (parse-v1 '((lambda (x) x) (+ 1 2))) (make-e-app (make-e-lam 'x (make-e-var 'x)) (make-e-op  '+ (make-e-num 1) (make-e-num 2))))


;;EXERCISE 3--------------------------------------------------------------------------------------
; A Val represents the value which would be substituted for a variable, which can be either a number or a lambda
;(define Val (signature (mixed ANumber ALambda)))

(: subst-v1 (ExprV1 ExprV1 Var -> ExprV1))
; substitutes all instances of the given symbol in an expression with a value.


    
(define (subst-v1 expr val var)
  (cond
     [(e-num? expr) expr]
     [(e-var? expr) (if (symbol=? (e-var-sym var) (e-var-sym expr))
                         val
                         expr)]
     [(e-lam? expr) (if (symbol=? (e-lam-param expr) (e-var-sym var))
                           expr
                           (make-e-lam (e-lam-param expr) (subst-v1 (e-lam-body expr) val var)))]
     [(e-op? expr) (make-e-op (e-op-op expr)
                              (subst-v1 (e-op-left expr) val var)
                              (subst-v1 (e-op-right expr) val var))]
     [(e-app? expr)  (make-e-app (subst-v1 (e-app-fun expr) val var)
                                     (subst-v1 (e-app-arg expr) val var))]
     [else (error "Invalid input")]))


(check-expect (subst-v1 (make-e-num 1) (make-e-num 3) (make-e-var 'a)) (make-e-num 1))
(check-expect (subst-v1 (make-e-var 'a) (make-e-num 4) (make-e-var 'a)) (make-e-num 4))
(check-expect (subst-v1 (parse-v1 '(lambda (g) (+ 1 2))) (make-e-num 30) (make-e-var 'g)) (parse-v1 '(lambda (g) (+ 1 2))))
(check-expect (subst-v1 (parse-v1 '(lambda (g) (+ 1 x))) (make-e-num 30) (make-e-var 'x)) (parse-v1 '(lambda (g) (+ 1 30))))
(check-expect (subst-v1 (parse-v1 '(* 1 x)) (make-e-num 20) (make-e-var 'x)) (parse-v1 '(* 1 20)))
(check-expect (subst-v1 (parse-v1 '(* y x)) (make-e-num 20) (make-e-var 'y)) (parse-v1 '(* 20 x)))
(check-expect (subst-v1 (parse-v1 '((lambda (x) (* 2 x)) g)) (make-e-num 10) (make-e-var 'g)) (parse-v1 '((lambda (x) (* 2 x)) 10)))
(check-expect (subst-v1 (parse-v1 '((lambda (x) y) z)) (make-e-num 10) (make-e-var 'z)) (parse-v1 '((lambda (x) y) 10)))
(check-expect (subst-v1 (parse-v1 '((lambda (x) y) 20)) (make-e-num 10) (make-e-var 'y)) (parse-v1 '((lambda (x) 10) 20)))
(check-expect (subst-v1 (parse-v1 '((lambda (x) x) x)) (make-e-num 10) (make-e-var 'x)) (parse-v1 '((lambda (x) x) 10)))
(check-expect (subst-v1 (parse-v1 '((lambda (x) y) 10)) (make-e-num 10) (make-e-var 'y)) (parse-v1 '((lambda (x) 10) 10)))
(check-expect (subst-v1 (parse-v1 '(lambda (x) x)) (make-e-num 10) (make-e-var 'x)) (parse-v1 '(lambda (x) x)))
(check-expect (subst-v1 (parse-v1 '(lambda (x) y)) (make-e-num 10) (make-e-var 'y)) (parse-v1 '(lambda (x) 10)))



;;EXERCISE 4--------------------------------------------------------------------------------------


(: eval-v1 (ExprV1 -> ValV1))
; evals an expression and returns a Val, which can either be a number of a make-e-lam.


(define (eval-v1 expr)
  (cond [(e-num? expr) expr]      
        [(e-lam? expr) expr]
        [(e-op? expr) (if (and (e-num? (eval-v1 (e-op-left expr))) (e-num? (eval-v1 (e-op-right expr))))
                          (cond
                            [(symbol=? (e-op-op expr) '+) (make-e-num (+ (e-num-num (eval-v1 (e-op-left expr))) (e-num-num (eval-v1 (e-op-right expr)))))]
                            [(symbol=? (e-op-op expr) '-) (make-e-num (- (e-num-num (eval-v1 (e-op-left expr))) (e-num-num (eval-v1 (e-op-right expr)))))]
                            [(symbol=? (e-op-op expr) '*) (make-e-num (* (e-num-num (eval-v1 (e-op-left expr))) (e-num-num (eval-v1 (e-op-right expr)))))]
                            [(symbol=? (e-op-op expr) '/) (make-e-num (/ (e-num-num (eval-v1 (e-op-left expr))) (e-num-num (eval-v1 (e-op-right expr)))))])
                          (error "invalid e-opation: " expr))]

        
        [(e-app? expr) (if (e-lam? (eval-v1 (e-app-fun expr)))
                           (eval-v1 (subst-v1 (e-lam-body (eval-v1 (e-app-fun expr)))
                                              (eval-v1 (e-app-arg expr))
                                              (make-e-var (e-lam-param (eval-v1 (e-app-fun expr))))))
                             (error "Invalid Function Application: " expr))]
        
        [else (error "Invalid input: " expr)]))  


(check-expect (eval-v1 (make-e-num 1)) (make-e-num 1))


(check-expect (eval-v1 (parse-v1 '(lambda (x) x))) (parse-v1 '(lambda (x) x)))


(check-expect (eval-v1 (parse-v1 '(lambda (x) (+ x 100)))) (parse-v1 '(lambda (x) (+ x 100))))
(check-expect (eval-v1 (parse-v1 '(+ 1 2))) (parse-v1 3))
(check-expect (eval-v1 (parse-v1 '(+ 1 ((lambda (v) (+ 1 v)) 10)))) (parse-v1 12))


(check-expect (eval-v1 (parse-v1 '(* ((lambda (v) (* 100 v)) 1) ((lambda (v) (/ v 10)) 100)))) (parse-v1 1000))
(check-expect (eval-v1 (parse-v1 '((lambda (x) x) 10))) (parse-v1 10))
(check-expect (eval-v1 (parse-v1 '((lambda (x) x) ((lambda (x) x) 10)))) (parse-v1 10))
(check-error (eval-v1 (parse-v1 '((lambda (v) (+ 3 v)) (lambda (x) x)))))
(check-error (eval-v1 (parse-v1 '(1 2))))
(check-expect (eval-v1 (parse-v1 '(lambda (x) (lambda (y) (lambda (z) (+ y z)))))) (parse-v1 '(lambda (x) (lambda (y) (lambda (z) (+ y z))))))
(check-expect (eval-v1 (parse-v1 '((((lambda (x) (lambda (y) (lambda (z) (+ y z)))) 1) 2) 3))) (parse-v1 5))
(check-expect  (eval-v1 (parse-v1 '((lambda (x) ((lambda (x) ((lambda (x) (+ x 1)) 2)) 3)) 4))) (parse-v1 3))
(check-error (eval-v1 (parse-v1 'c)))
(check-error (eval-v1 (parse-v1 '(= 1 2))))
(check-error (eval-v1 (parse-v1  '((lambda (x) (* x 10)) (+ 1 2 3)))))
(check-expect (eval-v1 (parse-v1 '((lambda (x) (* x 10)) (+ 1 2)))) (parse-v1 30))


;;EXERCISE 5--------------------------------------------------------------------------------------
(: wf-v1 (ExprV1 -> ExprV1))
; takes an Expr and either returns the same Expr if there are no unbound variables
; and returns an error if there are, in the form of a list of all unbound variables
(define (wf-v1 expr)
  (if (empty? (wf/acc-v1 expr '() '()))
      expr
      (error "List of unbound variables: " (wf/acc-v1 expr '() '()))))

(check-error (wf-v1 (parse-v1 '((lambda (x) ((lambda (x) ((lambda (x) (+ x 1)) 2)) z)) 4))))
(check-error (wf-v1 (parse-v1 '(lambda (x) y))))
(check-error (wf-v1 (parse-v1 '((lambda (x) y) z))))
(check-expect (wf-v1 (parse-v1 '(lambda (x) (lambda (y) (lambda (z) (+ y z)))))) (parse-v1 '(lambda (x) (lambda (y) (lambda (z) (+ y z))))))
(check-expect (wf-v1 (parse-v1 '((((lambda (x) (lambda (y) (lambda (z) (+ y z)))) 1) 2) 3))) (parse-v1 '((((lambda (x) (lambda (y) (lambda (z) (+ y z)))) 1) 2) 3)))
(check-expect (wf-v1 (parse-v1 '((lambda (x) ((lambda (x) ((lambda (x) (+ x 1)) 2)) 3)) 4))) (parse-v1 '((lambda (x) ((lambda (x) ((lambda (x) (+ x 1)) 2)) 3)) 4)))
(check-error (wf-v1 (parse-v1 '((((lambda (x) (lambda (y) (lambda (z) (+ y z)))) x) y) z))))
(check-expect (wf-v1 (parse-v1 '((lambda (x) (* x 10)) (+ 1 2)))) (parse-v1 '((lambda (x) (* x 10)) (+ 1 2))))
(check-error (wf-v1 (parse-v1 '((((lambda (x) (lambda (y) (lambda (z) (+ y z)))) o) g) l))))
(check-expect (wf-v1 (parse-v1 '(+ 1 2))) (parse-v1 '(+ 1 2)))
(check-error (wf-v1 (parse-v1 'n)))
(check-error (wf-v1 (parse-v1 '(* (* v x) (+ l g)))))
(check-error (wf-v1 (parse-v1 '(/ v x))))            
(check-error (wf-v1 (parse-v1 '(+ 12 c))))


(define (wf/acc-v1 expr lov loe)
; helper for wf-v1 which tracks the list of valid variables in a list 'lov'
; and tracks the list of unbound variables in a list of errors called 'loe'
  (cond
    [(e-num? expr) loe]
    [(e-var? expr) (if (member? (e-var-sym expr) lov) loe (list* (e-var-sym expr) loe))]
    [(e-lam? expr) (wf/acc-v1 (e-lam-body expr) (cons (e-lam-param expr) lov) loe)] 
    [(e-op? expr) (append (wf/acc-v1 (e-op-left expr) lov loe) (wf/acc-v1 (e-op-right expr) lov loe))]
    [(e-app? expr) (append (wf/acc-v1 (e-app-fun expr) lov loe) (wf/acc-v1 (e-app-arg expr) '() loe))]))


(check-expect (wf/acc-v1 (parse-v1 '((lambda (x) ((lambda (x) ((lambda (x) (+ x 1)) 2)) z)) 4)) '() '()) (list 'z))
(check-expect (wf/acc-v1 (parse-v1 '(lambda (x) y)) '() '()) (list 'y))
(check-expect (wf/acc-v1 (parse-v1 '((lambda (x) y) z)) '() '()) (list 'y 'z))
(check-expect (wf/acc-v1 (parse-v1 '(lambda (x) (lambda (y) (lambda (z) (+ y z))))) '() '()) '())
(check-expect (wf/acc-v1 (parse-v1 '((((lambda (x) (lambda (y) (lambda (z) (+ y z)))) 1) 2) 3)) '() '()) '())
(check-expect (wf/acc-v1 (parse-v1 '((lambda (x) ((lambda (x) ((lambda (x) (+ x 1)) 2)) 3)) 4)) '() '()) '())
(check-expect (wf/acc-v1 (parse-v1 '((((lambda (x) (lambda (y) (lambda (z) (+ y z)))) x) y) z)) '() '()) (list 'x 'y 'z))
(check-expect (wf/acc-v1 (parse-v1 '((lambda (x) (* x 10)) (+ 1 e))) '() '()) (list 'e))
(check-expect (wf/acc-v1 (parse-v1 'n) '() '()) (list 'n))
(check-expect (wf/acc-v1 (parse-v1 '(* (* v x) (+ l g))) '() '()) (list 'v 'x 'l 'g))
(check-expect (wf/acc-v1 (parse-v1 '(/ v x)) '() '()) (list 'v 'x))            
(check-expect (wf/acc-v1 (parse-v1 '(+ 12 c)) '() '()) (list 'c))

;;;;;;;;;;;;;;;;;;;;;;

;;   V2 Functions   ;;

;;;;;;;;;;;;;;;;;;;;;;  

(define-struct e-set! [id arg])
(define Var (signature (EVarOf Symbol)))
(define-struct e-loc [sym])
(define Loc (signature (ELocOf Symbol)))


(define ExprV2 (signature (mixed (ENumOf Number)
                      (EVarOf Symbol)
                      (EOpOf Op ExprV2 ExprV2)
                      (ELamOf Symbol ExprV2)
                      (EAppOf ExprV2 ExprV2)
                      (ESet!Of (mixed Var Loc) ExprV2)
                      (ELocOf Symbol))))

(define ValV2 (signature (mixed (ENumOf Number) (ELamOf Symbol ExprV2))))



(: parse-v2 (S-Exp -> ExprV2))
; parses an S-Exp, out of which valid formats include:
; - a number
; - a symbol
#|
- a list, and a list can be:
  - (list an-operation ExprV2 ExprV2), where an-operation is a symbol that can be '+ '- '* or '/
  - (list 'lambda lambda-argument ExprV2)
  - (list (list 'lambda lambda-argument ExprV2) input-value)
  - (list set! (A Var or Loc) ExprV2)
  - (list 'loc symbol)
|#
(define (parse-v2 sexp)
  (cond [(number? sexp) (make-e-num sexp)] ; numbers are left alone
        [(string? sexp) (error "Invalid String")] ; strings not allowed
        [(boolean? sexp) (error "Invalid Boolean")] ; booleans not allowed
        [(symbol? sexp) (make-e-var sexp)] ; symbols are left alone
        [(list? sexp) (cond [(= (length sexp) 3)
                             (cond
                               ; Checking whether its an arithmetic expression
                               [(op? (first sexp))  (make-e-op (first sexp)
                                                               (parse-v2 (second sexp))
                                                               (parse-v2 (third sexp)))]
                               
                               ; Checking whether its a one argument lambda
                               [(and (and (symbol? (first sexp)) (symbol=? 'lambda (first sexp)))
                                     (and (list? (second sexp))
                                          (= (length (second sexp)) 1)
                                          (symbol? (first (second sexp)))))
                                (make-e-lam (first (second sexp))
                                               (parse-v2 (third sexp)))]

                               ; Checking whether its a set!
                               [(and (and (symbol? (first sexp)) (symbol=? 'set! (first sexp)))
                                     (or (symbol? (second sexp))
                                         (e-loc? (parse-v2 (second sexp)))))
                                (make-e-set! (parse-v2 (second sexp)) (parse-v2 (third sexp)))]
                               
                               [else (error "Invalid List" sexp)])]
                            
                            [(= (length sexp) 2)
                             ; Checking whether its a function application or an e-loc
                             (if (and (symbol? (first sexp)) (symbol=? (first sexp) 'loc) (symbol? (second sexp)))
                                 (make-e-loc  (second sexp))
                                 (make-e-app (parse-v2 (first sexp))
                                             (parse-v2 (second sexp))))]
                             
                            [else (error "Invalid List")])]
                             
                            
        [else (error "Invalid S-Expression")])) ; any other input is an invalid S-Expression by definition
(check-expect (parse-v2 '(set! a 20)) (make-e-set! (make-e-var 'a) (make-e-num 20)))
(check-expect (parse-v2 '(+ 1 (set! b 20))) (make-e-op '+ (make-e-num 1) (make-e-set! (make-e-var 'b) (make-e-num 20))))
(check-expect (parse-v2 '(- (set! l 30) ((lambda (v) (+ 1 v)) 1))) (make-e-op '- (make-e-set! (make-e-var 'l) (make-e-num 30)) (make-e-app (make-e-lam 'v
                                                                                                                                          (make-e-op '+ (make-e-num 1)
                                                                                                                                                        (make-e-var 'v)))
                                                                                                                                          (make-e-num 1))))
(check-expect (parse-v2 '(* (set! b 20) (set! a 10))) (make-e-op '* (make-e-set! (make-e-var 'b) (make-e-num 20)) (make-e-set! (make-e-var 'a) (make-e-num 10))))
(check-expect (parse-v2 '(lambda (v) (set! a 20))) (make-e-lam 'v (make-e-set! (make-e-var 'a) (make-e-num 20))))
(check-expect (parse-v2 '((lambda (v) (set! b 20)) 20)) (make-e-app (make-e-lam 'v (make-e-set! (make-e-var 'b) (make-e-num 20))) (make-e-num 20)))
(check-expect (parse-v2 '((lambda (v) b) (set! b 20))) (make-e-app (make-e-lam 'v (make-e-var 'b)) (make-e-set! (make-e-var 'b) (make-e-num 20))))
(check-expect (parse-v2 '((lambda (v) (set! b 20)) (set! b 30))) (make-e-app (make-e-lam 'v (make-e-set! (make-e-var 'b) (make-e-num 20)))(make-e-set! (make-e-var 'b) (make-e-num 30))))
(check-expect (parse-v2 '(set! b (set! b 20))) (make-e-set! (make-e-var 'b) (make-e-set! (make-e-var 'b) (make-e-num 20))))
(check-error (parse-v2 '(set b 20)))
(check-expect (parse-v2 '(set! b (+ 1 2))) (make-e-set! (make-e-var 'b) (make-e-op '+ (make-e-num 1) (make-e-num 2))))
(check-error (parse-v2 '(set! 1 b)))
(check-error (parse-v2 '(set! b (lambda (x) x) 1)))
(check-error (parse-v2 (set! b 1 2)))


(: subst-v2 (ExprV2 (ELocOf Symbol) Var -> ExprV2))
; substitutes all instances of the given symbol in an expression with a value.
    
(define (subst-v2 expr val var)
  (cond
     [(e-num? expr) expr]
     [(e-var? expr) (if (symbol=? (e-var-sym var) (e-var-sym expr))
                         val
                         expr)]
     [(e-lam? expr) (if (symbol=? (e-lam-param expr) (e-var-sym var))
                           expr
                           (make-e-lam (e-lam-param expr) (subst-v2 (e-lam-body expr) val var)))]
     [(e-op? expr) (make-e-op (e-op-op expr)
                              (subst-v2 (e-op-left expr) val var)
                              (subst-v2 (e-op-right expr) val var))]
     [(e-app? expr)  (make-e-app (subst-v2 (e-app-fun expr) val var)
                                     (subst-v2 (e-app-arg expr) val var))]

     [(e-set!? expr) (make-e-set! (subst-v2 (e-set!-id expr) val var) (subst-v2 (e-set!-arg expr) val var))]
     
     [(e-loc? expr) expr]
     
     [else (error "Invalid input")]))




(check-expect (subst-v2 (parse-v2 '2) (make-e-loc 'b302830) (make-e-var 'b)) (parse-v2 '2))
(check-expect (subst-v2 (parse-v2 'a) (make-e-loc 'c392856) (make-e-var 'c)) (parse-v2 'a))
(check-expect (subst-v2 (parse-v2 'a) (make-e-loc 'a392856) (make-e-var 'a)) (parse-v2 '(loc a392856)))
(check-expect (subst-v2 (parse-v2 '((lambda (c) (set! a c)) l)) (make-e-loc 'l395728) (make-e-var 'l)) (parse-v2 '((lambda (c) (set! a c)) (loc l395728))))
(check-expect (subst-v2 (parse-v2 '(set! a b)) (make-e-loc 'b932846) (make-e-var 'b)) (parse-v2 '(set! a (loc b932846))))
(check-expect (subst-v2 (parse-v2 '(+ 2 (set! a b))) (make-e-loc 'b098754) (make-e-var 'b)) (parse-v2 '(+ 2 (set! a (loc b098754)))))
(check-expect (subst-v2 (parse-v2 '(lambda (v) (set! a v))) (make-e-loc 'v932487) (make-e-var 'v)) (parse-v2 '(lambda (v) (set! a v))))
(check-expect (subst-v2 (parse-v2 '(loc b380275)) (make-e-loc 'a379738) (make-e-var 'a)) (parse-v2 '(loc b380275)))
(check-expect (subst-v2 (parse-v2 '(+ x y)) (make-e-loc 'x123456) (make-e-var 'x)) (parse-v2 '(+ (loc x123456) y)))
(check-expect (subst-v2 (parse-v2 '(lambda (y) x)) (make-e-loc 'x123456) (make-e-var 'x)) (parse-v2 '(lambda (y) (loc x123456))))
(check-expect (subst-v2 (parse-v2 '(lambda (x) x)) (make-e-loc 'y789012) (make-e-var 'x)) (parse-v2 '(lambda (x) x)))

              
(: wf-v2 (ExprV2 -> ExprV2))
; takes an Expr and either returns the same Expr if there are no unbound variables
; and returns an error if there are, in the form of a list of all unbound variables
(define (wf-v2 expr)
  (if (empty? (wf/acc-v2 expr '() '()))
      expr
      (error "List of unbound variables: " (wf/acc-v2 expr '() '()))))

(check-expect (wf-v2 (parse-v2 '(lambda (v) (set! v 20)))) (make-e-lam 'v (make-e-set! (make-e-var 'v) (make-e-num 20))))
(check-error (wf-v2 (parse-v2 '((lambda (v) (+ 1 v)) (set! v 20)))))
(check-error (wf-v2 (parse-v2 '((lambda (v) (+ a v)) (set! a 20)))))
(check-error (wf-v2 (parse-v2 '((lambda (v) (set! a 30)) 20))))
(check-error (wf-v2 (parse-v2 '(set! a 20))))
(check-error (wf-v2 (parse-v2 '(set! x 1))))
(check-error (wf-v2 (parse-v2 '(loc a329320))))
(check-error (wf-v2 (parse-v2 '(loc b3q99i2p))))
(check-expect (wf-v2 (parse-v2 '(lambda (x) (set! x (+ x 1))))) (parse-v2 '(lambda (x) (set! x (+ x 1)))))
(check-error (wf-v2 (parse-v2 '(+ (set! y 5) (set! z (* y z))))))                      
(check-error (wf-v2 (parse-v2 '(lambda (x) (+ (set! x 10) (lambda (y) (set! z (* x y))))))))
(check-expect (wf-v2 (parse-v2 '(lambda (c) (set! c 10))))(parse-v2 '(lambda (c) (set! c 10))))




(define (wf/acc-v2 expr lov loe)
; helper for wf-v2 which tracks the list of valid variables in a list 'lov'
; and tracks the list of unbound variables in a list of errors called 'loe'
  (cond
    [(e-num? expr) loe]
    [(e-var? expr) (if (member? (e-var-sym expr) lov) loe (list* (e-var-sym expr) loe))]
    [(e-lam? expr) (wf/acc-v2 (e-lam-body expr) (cons (e-lam-param expr) lov) loe)] 
    [(e-op? expr) (append (wf/acc-v2 (e-op-left expr) lov loe) (wf/acc-v2 (e-op-right expr) lov loe))]
    [(e-app? expr) (append (wf/acc-v2 (e-app-fun expr) lov loe) (wf/acc-v2 (e-app-arg expr) '() loe))]
    [(e-set!? expr) (if (member? (e-var-sym (e-set!-id expr)) lov) loe (list* (e-var-sym (e-set!-id expr)) loe))]
    [(e-loc? expr) (error "Invalid Input")]))

(check-expect (wf/acc-v2 (parse-v2 '2) '() '()) '())
(check-expect (wf/acc-v2 (parse-v2 'x) '(x) '()) '())
(check-expect (wf/acc-v2 (parse-v2 'x) '() '()) '(x))
(check-expect (wf/acc-v2 (parse-v2 '(lambda (x) x)) '() '()) '())
(check-expect (wf/acc-v2 (parse-v2 '(lambda (x) y)) '(y) '()) '())
(check-expect (wf/acc-v2 (parse-v2 '(+ x y)) '(x) '()) '(y))
(check-expect (wf/acc-v2 (parse-v2 '(f x)) '(f) '()) '(x))
(check-expect (wf/acc-v2 (parse-v2 '(set! x 1)) '(x) '()) '())
(check-expect (wf/acc-v2 (parse-v2 '(set! x 1)) '() '()) '(x))
(check-error (wf/acc-v2 (parse-v2 '(loc a329320))))
(check-error (wf/acc-v2 (parse-v2 '(loc b3q99i2p))))
(check-expect (wf/acc-v2 (parse-v2 '(lambda (x) (set! x (+ x 1)))) '() '()) '())
(check-expect (wf/acc-v2 (parse-v2 '(+ (set! y 5) (set! z (* y z)))) '(x) '()) '(y z))                       
(check-expect (wf/acc-v2 (parse-v2 '(lambda (x) (+ (set! x 10) (lambda (y) (set! z (* x y)))))) '() '()) '(z))

(define Store (signature (HashMapOf Symbol ValV2)))
(define-struct res [store val])
(define Result (signature (ResOf Store ValV2)))

(: eval-v2 (ExprV2 Store -> Result))
; evaluates an expression by storing each level of evaluating into a Store (a hashmap), and returns a Result


(define (eval-v2 expr store)
  (cond [(e-num? expr) (make-res store expr)]      
        [(e-lam? expr) (make-res store expr)]
        [(e-op? expr) (local [(define left (eval-v2 (e-op-left expr) store))
                              (define right (eval-v2 (e-op-right expr) (res-store left)))]
                        (if (and (e-num? (res-val left)) (e-num? (res-val right)))
                            (cond
                              [(symbol=? (e-op-op expr) '+) (make-res (res-store right) (make-e-num (+ (e-num-num (res-val left)) (e-num-num (res-val right)))))]
                              [(symbol=? (e-op-op expr) '-) (make-res (res-store right) (make-e-num (- (e-num-num (res-val left)) (e-num-num (res-val right)))))]
                              [(symbol=? (e-op-op expr) '*) (make-res (res-store right) (make-e-num (* (e-num-num (res-val left)) (e-num-num (res-val right)))))]
                              [(symbol=? (e-op-op expr) '/) (make-res (res-store right) (make-e-num (/ (e-num-num (res-val left)) (e-num-num (res-val right)))))])
                            (error "invalid operation: " expr)))]

        
        [(e-app? expr) (local [(define f (eval-v2 (e-app-fun expr) store))]
                         (if (e-lam? (res-val f))
                             (local [(define body (e-lam-body (res-val f)))
                                     (define arg (eval-v2 (e-app-arg expr) (res-store f)))
                                     (define par (gensym (e-lam-param (res-val f))))]
                               (eval-v2 (subst-v2 body
                                                  (make-e-loc par)
                                                  (make-e-var (e-lam-param (res-val f))))
                                        (hash-set (res-store arg) par (res-val arg)))) ; replaces all variable instances with the generated symbol to later replace with the value that is being mapped to it
                             (error "Invalid Function Application: " expr)))]
        [(e-loc? expr) (make-res store (hash-ref store (e-loc-sym expr)))] ; replaces the gensym with the value mapped to it in the hashmap
        [(e-set!? expr) (make-res (hash-set store (e-loc-sym (e-set!-id expr)) (res-val (eval-v2 (e-set!-arg expr) store))) (make-e-num 0))] ; maps the identifier of the set! to the value stated in the function and returns 0
        
        
        [else (error "Invalid input: " expr)]))  

(check-expect (eval-v2 (parse-v2 '5) [hash]) (make-res [hash] (parse-v2 '5)))
(check-expect (eval-v2 (parse-v2 '(lambda (x) 5)) [hash]) (make-res [hash] (parse-v2 '(lambda (x) 5))))
(check-expect (eval-v2 (parse-v2 '(+ 3 2)) [hash]) (make-res [hash] (parse-v2 '5)))
(check-expect (eval-v2 (parse-v2 '(- 5 2)) [hash]) (make-res [hash] (parse-v2 '3)))
(check-expect (eval-v2 (parse-v2 '(* 3 2)) [hash]) (make-res [hash] (parse-v2 '6)))
(check-expect (eval-v2 (parse-v2 '(/ 6 2)) [hash]) (make-res [hash] (parse-v2 '3)))
(check-expect (res-val (eval-v2 (parse-v2 '(lambda (x) x)) [hash])) (parse-v2 '(lambda (x) x)))
(check-expect (res-val (eval-v2 (parse-v2 '((lambda (x) x) 5)) (hash)))  (make-e-num 5))
(check-expect (res-val (eval-v2 (parse-v2 '(* ((lambda (v) (* 100 v)) 1) ((lambda (v) (/ v 10)) 100))) (hash))) (parse-v1 1000))
(check-expect (res-val (eval-v2 (parse-v2 '((lambda (x) x) 10)) (hash))) (parse-v1 10))
(check-expect (res-val (eval-v2 (parse-v2 '((lambda (x) x) ((lambda (x) x) 10))) (hash))) (parse-v1 10))
(check-expect (res-val (eval-v2 (parse-v2 '(lambda (c) (+ 1 (set! c 2)))) (hash))) (parse-v2 '(lambda (c) (+ 1 (set! c 2)))))
(check-expect (res-val (eval-v2 (parse-v2 '((lambda (a) (+ (set! a (+ 1 20)) a)) 2)) (hash))) (parse-v2 21))
(check-expect (res-val (eval-v2 (parse-v2 '((lambda (v) (* (set! v (- 1 2)) v)) 2))  (hash))) (parse-v2 0))

;;;;;;;;;;;;;;;;;;;;;;

;;   V3 Functions   ;;

;;;;;;;;;;;;;;;;;;;;;;

(define-struct e-box [val])
(define ABox (signature (EBoxOf ExprV3)))
(define-struct e-unbox [box])
(define-struct e-set-box![id arg])
(define-struct e-box-loc [sym])
(define ABoxLocation (signature (EBoxLocOf Symbol)))

(define ExprV3 (signature (mixed (ENumOf Number)
                      (EVarOf Symbol)
                      (EOpOf Op ExprV3 ExprV3)
                      (ELamOf Symbol ExprV3)
                      (EAppOf ExprV3 ExprV3)
                      (ESet!Of (mixed Var Loc) ExprV3)
                      (ELocOf Symbol)
                      (EBoxOf ExprV3)
                      (EUnboxOf ExprV3)
                      (ESetBox!Of ExprV3 ExprV3)
                      ABoxLocation)))

(define ValV3 (signature (mixed (ENumOf Number) (ELamOf Symbol ExprV3) ABoxLocation)))

(: parse-v3 (S-Exp -> ExprV3))
; parses an S-Exp, out of which valid formats include:
; - a number
; - a symbol
#|
- a list, and a list can be:
  - (list an-operation ExprV3 ExprV3), where an-operation is a symbol that can be '+ '- '* or '/
  - (list 'lambda lambda-argument ExprV3)
  - (list (list 'lambda lambda-argument ExprV3) input-value)
  - (list set! (A Var or Loc) ExprV3)
  - (list 'loc symbol)
  - (list box ExprV3)
  - (list unbox ExprV3)
  - (list set-box! ExprV3 ExprV3)
  - (list 'box-loc symbol)
|#
(define (parse-v3 sexp)
  (cond [(number? sexp) (make-e-num sexp)] ; numbers are left alone
        [(string? sexp) (error "Invalid String")] ; strings not allowed
        [(boolean? sexp) (error "Invalid Boolean")] ; booleans not allowed
        [(symbol? sexp) (make-e-var sexp)] ; symbols are left alone
        [(list? sexp) (cond [(= (length sexp) 3)
                             (cond
                               ; Checking whether its an arithmetic expression
                               [(op? (first sexp))  (make-e-op (first sexp)
                                                               (parse-v3 (second sexp))
                                                               (parse-v3 (third sexp)))]
                               
                               ; Checking whether its a one argument lambda
                               [(and (and (symbol? (first sexp)) (symbol=? 'lambda (first sexp)))
                                     (and (list? (second sexp))
                                          (= (length (second sexp)) 1)
                                          (symbol? (first (second sexp)))))
                                (make-e-lam (first (second sexp))
                                               (parse-v3 (third sexp)))]

                               ; Checking whether its a set!
                               [(and (and (symbol? (first sexp)) (symbol=? 'set! (first sexp)))
                                     (or (symbol? (second sexp))
                                         (e-loc? (parse-v3 (second sexp)))))
                                (make-e-set! (parse-v3 (second sexp)) (parse-v3 (third sexp)))]

                               ; Checking whether its a set-box!
                               [(and (symbol? (first sexp)) (symbol=? 'set-box! (first sexp)))
                                     
                                (make-e-set-box! (parse-v3 (second sexp)) (parse-v3 (third sexp)))]
                               
                               [else (error "Invalid List" sexp)])]
                            
                            [(= (length sexp) 2)
                             
                             ; Checking whether its an e-loc
                             (cond [(and (symbol? (first sexp)) (symbol=? (first sexp) 'loc) (symbol? (second sexp))) (make-e-loc  (second sexp))]  ; Checking whether its an e-loc
                                   [(and (symbol? (first sexp)) (symbol=? (first sexp) 'box-loc) (symbol? (second sexp))) (make-e-box-loc  (second sexp))]  ; Checking whether its an e-box-loc
                                   [(and (symbol? (first sexp)) (symbol=? (first sexp) 'box)) (make-e-box (parse-v3 (second sexp)))]  ; Checking whether its an e-box
                                   [(and (symbol? (first sexp)) (symbol=? (first sexp) 'unbox)) (make-e-unbox (parse-v3 (second sexp)))]  ; Checking whether its an e-unbox
                                   [else (make-e-app (parse-v3 (first sexp))
                                                     (parse-v3 (second sexp)))])]  ; Else it creates a function-application
                             
                            [else (error "Invalid List")])]
                             
                            
        [else (error "Invalid S-Expression")])) ; any other input is an invalid S-Expression by definition

(check-expect (parse-v3 '(set-box! a 30)) (make-e-set-box! (make-e-var 'a) (make-e-num 30)))
(check-expect (parse-v3 '(set-box! a a)) (make-e-set-box! (make-e-var 'a) (make-e-var 'a)))
(check-expect (parse-v3 '(set-box! a (+ 1 20))) (make-e-set-box! (make-e-var 'a) (make-e-op '+ (make-e-num 1) (make-e-num 20))))
(check-expect (parse-v3 '(loc a849203)) (make-e-loc 'a849203))
(check-expect (parse-v3 '(box-loc a484029)) (make-e-box-loc 'a484029))
(check-expect (parse-v3 '(box (+ 1 2))) (make-e-box (make-e-op '+ (make-e-num 1) (make-e-num 2))))
(check-expect (parse-v3 '(unbox (lambda (c) c))) (make-e-unbox (make-e-lam 'c (make-e-var 'c))))
(check-error (parse-v3 '(loc a a)))
(check-expect (parse-v3 '(lambda (x) (loc x))) (make-e-lam 'x (make-e-loc 'x)))
(check-expect (parse-v3 '(lambda (y) (+ 1 (loc y479473)))) (make-e-lam 'y (make-e-op '+ (make-e-num 1) (make-e-loc 'y479473))))
(check-expect (parse-v3 '(lambda (z) (box-loc z))) (make-e-lam 'z (make-e-box-loc 'z)))
(check-expect (parse-v3 '(lambda (a) (- (box-loc a) 5))) (make-e-lam 'a (make-e-op '- (make-e-box-loc 'a) (make-e-num 5))))
(check-expect (parse-v3 '(lambda (b) (box (unbox b)))) (make-e-lam 'b (make-e-box (make-e-unbox (make-e-var 'b)))))
(check-expect (parse-v3 '(lambda (c) (unbox (box (* c 2))))) (make-e-lam 'c (make-e-unbox (make-e-box (make-e-op '* (make-e-var 'c) (make-e-num 2))))))
(check-expect (parse-v3 '(lambda (x) (lambda (y) (+ (loc x) (unbox (box (* y (box-loc x))))))))
 (make-e-lam 'x (make-e-lam 'y (make-e-op '+ (make-e-loc 'x) (make-e-unbox (make-e-box (make-e-op '* (make-e-var 'y) (make-e-box-loc 'x))))))))
(check-expect (parse-v3 '(lambda (x) (box-loc (lambda (y) (loc x4829293)))))
 (make-e-lam 'x (make-e-app (make-e-var 'box-loc) (make-e-lam 'y (make-e-loc 'x4829293)))))
(check-expect (parse-v3 '(lambda (m) (lambda (n) (- (unbox (box (+ m n))) (loc m3829283)))))
 (make-e-lam 'm (make-e-lam 'n (make-e-op '- (make-e-unbox (make-e-box (make-e-op '+ (make-e-var 'm) (make-e-var 'n)))) (make-e-loc 'm3829283)))))



(: subst-v3 (ExprV3 (mixed (ELocOf Symbol) ABoxLocation) Var -> ExprV3))
; substitutes all instances of the given symbol in an expression with a value.
    
(define (subst-v3 expr val var)
  (cond
     [(e-num? expr) expr]
     [(e-var? expr) (if (symbol=? (e-var-sym var) (e-var-sym expr))
                         val
                         expr)]
     [(e-lam? expr) (if (symbol=? (e-lam-param expr) (e-var-sym var))
                           expr
                           (make-e-lam (e-lam-param expr) (subst-v3 (e-lam-body expr) val var)))]
     [(e-op? expr) (make-e-op (e-op-op expr)
                              (subst-v3 (e-op-left expr) val var)
                              (subst-v3 (e-op-right expr) val var))]
     [(e-app? expr)  (make-e-app (subst-v3 (e-app-fun expr) val var)
                                     (subst-v3 (e-app-arg expr) val var))]

     [(e-set!? expr) (make-e-set! (subst-v3 (e-set!-id expr) val var) (subst-v3 (e-set!-arg expr) val var))]

     [(e-loc? expr) expr]
     
     [(e-box-loc? expr) expr]

     [(e-box? expr) (make-e-box (subst-v3 (e-box-val expr) val var))]
     
     [(e-unbox? expr) (make-e-unbox (subst-v3 (e-unbox-box expr) val var))]
     
     [(e-set-box!? expr) (make-e-set-box! (subst-v3 (e-set-box!-id expr) val var) (subst-v3 (e-set-box!-arg expr) val var))]
     
     [else (error "Invalid input")]))




(check-expect (subst-v3 (parse-v3 '(set! a b)) (make-e-loc 'b932846) (make-e-var 'b)) (parse-v2 '(set! a (loc b932846))))
(check-expect (subst-v3 (parse-v3 '(+ 2 (set! a b))) (make-e-loc 'b098754) (make-e-var 'b)) (parse-v2 '(+ 2 (set! a (loc b098754)))))
(check-expect (subst-v3 (parse-v3 '(lambda (v) (set! a v))) (make-e-loc 'v932487) (make-e-var 'v)) (parse-v2 '(lambda (v) (set! a v))))
(check-expect (subst-v3 (parse-v3 '(loc a849201)) (make-e-loc 'b39202) (make-e-var 'b)) (parse-v3 '(loc a849201)))
(check-expect (subst-v3 (parse-v3 '(box-loc d320221)) (make-e-loc 'd329203) (make-e-var 'd)) (parse-v3 '(box-loc d320221)))
(check-expect (subst-v3 (parse-v3 '(box a)) (make-e-loc 'a392021) (make-e-var 'a)) (parse-v3 '(box (loc a392021))))
(check-expect (subst-v3 (parse-v3 '(unbox (box a))) (make-e-loc 'a393020) (make-e-var 'a)) (parse-v3 '(unbox (box (loc a393020)))))
(check-expect (subst-v3 (parse-v3 '(set-box! a (lambda (c) (+ 1 c)))) (make-e-loc 'c22921) (make-e-var 'c))
              (parse-v3 '(set-box! a (lambda (c) (+ 1 c)))))
(check-expect (subst-v3 (parse-v3 '(set-box! a (lambda (c) (+ 1 c)))) (make-e-loc 'a22921) (make-e-var 'a))
              (parse-v3 '(set-box! (loc a22921) (lambda (c) (+ 1 c)))))

              
(: wf-v3 (ExprV3 -> ExprV3))
; takes an Expr and either returns the same Expr if there are no unbound variables
; and returns an error if there are, in the form of a list of all unbound variables
(define (wf-v3 expr)
  (if (empty? (wf/acc-v3 expr '() '()))
      expr
      (error "List of unbound variables: " (wf/acc-v3 expr '() '()))))

(check-expect (wf-v3 (parse-v3 '(lambda (x) (box x)))) (parse-v3 '(lambda (x) (box x))))
(check-error (wf-v3 (parse-v3 '(unbox y))))
(check-error (wf-v3 (parse-v3 '(lambda (x) (set-box! x (op + y 1))))))
(check-error (wf-v3 (parse-v3 '(box-loc x))))
(check-expect (wf-v3 (parse-v3 '(lambda (x) (box x)))) (parse-v3 '(lambda (x) (box x))))
(check-expect (wf-v3 (parse-v3 '(lambda (x) (unbox (box x))))) (parse-v3 '(lambda (x) (unbox (box x)))))
(check-expect (wf-v3 (parse-v3 '(lambda (x) (lambda (y) (set-box! x y))))) (parse-v3 '(lambda (x) (lambda (y) (set-box! x y)))))
(check-expect (wf-v3 (parse-v3 '(lambda (z) (set-box! z (lambda (w) (+ z w))))))  (parse-v3 '(lambda (z) (set-box! z (lambda (w) (+ z w))))))
(check-expect (wf-v3 (parse-v3 '(lambda (a) (lambda (b) (unbox (box (+ a b))))))) (parse-v3 '(lambda (a) (lambda (b) (unbox (box (+ a b)))))))






(define (wf/acc-v3 expr lov loe)
; helper for wf-v3 which tracks the list of valid variables in a list 'lov'
; and tracks the list of unbound variables in a list of errors called 'loe'
  (cond
    [(e-num? expr) loe]
    [(e-var? expr) (if (member? (e-var-sym expr) lov) loe (list* (e-var-sym expr) loe))]
    [(e-lam? expr) (wf/acc-v3 (e-lam-body expr) (cons (e-lam-param expr) lov) loe)] 
    [(e-op? expr) (append (wf/acc-v3 (e-op-left expr) lov loe) (wf/acc-v3 (e-op-right expr) lov loe))]
    [(e-app? expr) (append (wf/acc-v3 (e-app-fun expr) lov loe) (wf/acc-v3 (e-app-arg expr) '() loe))]
    [(e-set!? expr) (if (member? (e-var-sym (e-set!-id expr)) lov) loe (list* (e-var-sym (e-set!-id expr)) loe))]
    [(e-loc? expr) (error "Invalid Input")]
    [(e-box-loc? expr) (error "Invalid Input")]
    [(e-box? expr) (wf/acc-v3 (e-box-val expr) lov loe)]
    [(e-unbox? expr) (wf/acc-v3 (e-unbox-box expr) lov loe)]
    [(e-set-box!? expr) (if (member? (e-var-sym (e-set-box!-id expr)) lov) loe (list* (e-var-sym (e-set-box!-id expr)) loe))]))

(check-expect (wf/acc-v3 (parse-v3 '(box x)) '(x) '())  '())
(check-expect (wf/acc-v3 (parse-v3 '(unbox y)) '() '()) '(y))
(check-expect (wf/acc-v3 (parse-v3 '(set-box! x 5)) '(x) '()) '())
(check-expect (wf/acc-v3 (parse-v3 '(set-box! x (unbox (box y)))) '(x y) '()) '())
(check-expect (wf/acc-v3 (parse-v3 '(box (unbox (box (+ a b))))) '(a b) '())  '())
(check-expect (wf/acc-v3 (parse-v3 '(lambda (x) (box x))) '(x) '()) '())
(check-expect (wf/acc-v3 (parse-v3 '(lambda (x) (unbox (box c)))) '(x) '()) '(c))
(check-expect (wf/acc-v3 (parse-v3 '(lambda (x) (lambda (y) (set-box! s y)))) '(x y) '()) '(s))
(check-expect (wf/acc-v3 (parse-v3 '(lambda (z) (set-box! z (lambda (w) (+ z w))))) '(z w) '(z)) '(z))
(check-expect (wf/acc-v3 (parse-v3 '(lambda (a) (lambda (b) (unbox (box (+ c d)))))) '(a b) '()) '(c d)) 


(define StoreV3 (signature (HashMapOf Symbol ValV3)))
(define ResultV3 (signature (ResOf StoreV3 ValV3)))

(: eval-v3 (ExprV3 StoreV3 -> ResultV3))
; evals an expression and returns a Val, which can either be a number of a make-e-lam.


(define (eval-v3 expr store)
  (cond [(e-num? expr) (make-res store expr)]      
        [(e-lam? expr) (make-res store expr)]
        [(e-op? expr) (local [(define left (eval-v3 (e-op-left expr) store))
                              (define right (eval-v3 (e-op-right expr) (res-store left)))]
                        (if (and (e-num? (res-val left)) (e-num? (res-val right)))
                            (cond
                              [(symbol=? (e-op-op expr) '+) (make-res (res-store right) (make-e-num (+ (e-num-num (res-val left)) (e-num-num (res-val right)))))]
                              [(symbol=? (e-op-op expr) '-) (make-res (res-store right) (make-e-num (- (e-num-num (res-val left)) (e-num-num (res-val right)))))]
                              [(symbol=? (e-op-op expr) '*) (make-res (res-store right) (make-e-num (* (e-num-num (res-val left)) (e-num-num (res-val right)))))]
                              [(symbol=? (e-op-op expr) '/) (make-res (res-store right) (make-e-num (/ (e-num-num (res-val left)) (e-num-num (res-val right)))))])
                            (error "invalid operation: " expr)))]

        
        [(e-app? expr) (local [(define f (eval-v3 (e-app-fun expr) store))]
                         (if (e-lam? (res-val f))
                             (local [(define body (e-lam-body (res-val f)))
                                     (define arg (eval-v3 (e-app-arg expr) (res-store f)))
                                     (define par (gensym (e-lam-param (res-val f))))]
                               (eval-v3 (subst-v3 body
                                                  (make-e-loc par)
                                                  (make-e-var (e-lam-param (res-val f)))) 
                                        (hash-set (res-store arg) par (res-val arg)))) ; replaces all variable instances with the generated symbol to later replace with the value that is being mapped to it
                             (error "Invalid Function Application: " expr)))]
        
        [(e-loc? expr) (make-res store (hash-ref store (e-loc-sym expr)))] ; replaces the gensym with the value mapped to it in the hashmap
        
        [(e-set!? expr) (make-res (hash-set store (e-loc-sym (e-set!-id expr)) (res-val (eval-v3 (e-set!-arg expr) store))) (make-e-num 0))] ; maps the value of the set! to the identifier stated in the function and returns 0

        [(e-box-loc? expr) expr]

        [(e-box? expr) (local [(define sym (gensym))]
                              (make-res (hash-set store sym (res-val (eval-v3 (e-box-val expr) store))) (make-e-box-loc sym)))] ; evaluates the argument in the box and maps it to a generated symbol encapsulated in a box-loc
     
        [(e-unbox? expr) (local [(define location (eval-v3 (e-unbox-box expr) store))]
                           (if (e-box-loc? (res-val location))
                             (make-res (res-store location) (hash-ref store (e-box-loc-sym (res-val location)))) ; returns the value mapped to the symbol in the box-loc
                             (error "Unable to unbox")))]
     
        [(e-set-box!? expr) (local [(define box-id (eval-v3 (e-set-box!-id expr) store))
                                    (define box-arg (eval-v3 (e-set-box!-arg expr) (res-store box-id)))]
                                    (if (e-box-loc? (res-val box-id))
                                      (make-res (hash-set (res-store box-arg) (e-box-loc-sym (res-val box-id)) (res-val box-arg)) (make-e-num 0)) ; maps the value of the set-box! to the identifier stated in the function and returns 0
                                      (error "Unable to set box")))]
        
        
        [else (error "Invalid input: " expr)]))



(check-expect (res-val (eval-v3 (parse-v3 '((lambda (y)
                                               ((lambda (x)
                                                  (+ (unbox y)
                                                     (+ (+ (unbox x)
                                                           (+ (set-box! x (+ (unbox y)
                                                                             (set-box! y 5)))
                                                              (unbox x)))
                                                        (unbox y))))
                                                (box 2)))
                                             (box 3))) (hash)))
              (make-e-num 13))


(check-expect (res-val (eval-v3 (parse-v3 '((lambda (x)
                                              ((lambda (y)
                                                 (* (unbox x)
                                                    (- (unbox y)
                                                       (+ (set-box! y (- (unbox x) (set-box! x 10)))
                                                          (unbox y)))))
                                               (box 6)))
                                            (box 8))) (hash)))
              (make-e-num -16))

(check-expect (res-val (eval-v3 (parse-v3 '((lambda (a)
                                              ((lambda (b)
                                                 (+ (unbox a)
                                                    (+ (unbox b)
                                                       (+ (set-box! b (+ (unbox a) (set-box! a 7)))
                                                          (unbox b)))))
                                               (box 4)))
                                            (box 5))) (hash)))
              (make-e-num 14))

(check-expect (res-val (eval-v3 (parse-v3 '((lambda (x) (set-box! (box x) 20)) 15)) (hash))) (make-e-num 0))
(check-expect (res-val (eval-v3 (parse-v3 '((lambda (x) ((lambda (y) (unbox y)) (box x))) 25)) (hash))) (make-e-num 25))
(check-expect (res-val (eval-v3 (parse-v3 '((lambda (x) ((lambda (y) (unbox y)) (box x))) (+ 1 24))) (hash))) (make-e-num 25))
(check-expect (res-val (eval-v3 (parse-v3 '((lambda (x) (set-box! (box x) (* 1 10000))) (/ 300 10))) (hash))) (make-e-num 0))
(check-expect (res-val (eval-v3 (parse-v3 '((lambda (x) ((lambda (y) (unbox y)) (box x))) ((lambda (y) y) 25))) (hash))) (make-e-num 25))
(check-expect (res-val (eval-v3 (parse-v3 '((lambda (f) ((unbox f) 400)) (box (lambda (x) x)))) (hash))) (make-e-num 400))
(check-expect (res-val (eval-v3 (parse-v3 '((lambda (x) (set-box! (box x) 40)) (+ 1 100))) (hash))) (make-e-num 0))
(check-expect (res-val (eval-v3 (parse-v3 '((lambda (x) (set-box! (box x) (* 1 10000))) (/ 300 10))) (hash))) (make-e-num 0))
(check-expect (res-val (eval-v3 (parse-v3 '(((lambda (f) (lambda (g)
                                                           (+ (+ (set-box! f 31) (unbox f))
                                                              (+ (set-box! g 28) (unbox g)))))
                                             (box 3)) (box 4))) (hash)))
              (parse-v3 59))

(check-expect (res-val (eval-v3 (parse-v3 '(((lambda (f) (lambda (g)
                                                           (* (+ (set-box! f 1) (unbox f))
                                                              (/ (set-box! g 38) (unbox g)))))
                                             (box 5)) (box 4))) (hash)))
              (parse-v3 0))

(check-expect (res-val (eval-v3 (parse-v3 '(((lambda (f) (lambda (g)
                                                           (* (+ (set-box! f 1) 5)
                                                              (/ (set-box! g 2) 1))))
                                             (box 5)) (box 4))) (hash)))
              (parse-v3 0))


