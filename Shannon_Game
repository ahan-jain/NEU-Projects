#lang htdp/isl+
(require 2htdp/image)
(require 2htdp/universe)
(define Image (signature (predicate image?)))
(define APosn (signature (predicate posn?)))

#| CHANGES MADE
1. Fixed all the bugs that were present in the 6B submission in the following ways:
- fixed the parameters of upgrade-grid (it's supposed to take two copies of the grid but only one was defined in the function definition)
- made all cell charge functions (cp-charge, output-charge, gates-charge) and charge-row function take in an extra copy of the grid to
  reference the original grid in terms of posns instead of the modified grid (which gets modified since the recursive call removes the first
  sublist in the successive evaluation)
- modified the base condition for the charge-row and update-grid functions. We felt that since we're updating the 'pos' parameter in each recursive call
  to traverse through the grid and its rows, it would be more apt to represent the base conditions, in that it returns an empty list if the posns are no
  longer valid
- changed the way the gates-charge functions calculates the charge that it propogates to the output cell. Initially, we used the filter list abstraction
  that prevented the output cell from being evaulated using equal?. But now, the input cells are calculated using the helper function 'filter-input'
  which returns a list of cells that exclude the output cell based on the direction of the gate (which indicates which direction its output is).
- delegated the calculation of charges for gates to helpers for each type of gate (and-charge, or-charge, not-charge)
- created a new helper which checks if all neighbouring input cells of a gate are charged, since the charge can only propogate then
  using 'all-neighbors-null?'
- Added a new function 'charge-of-cell' which is a helper function for 'draw-row', which draws each cell with a red, blue or black border
  depending on its charge
- Added check-expects for all functions.
  
2. Incorporated other types of feedback such as:
- added comments for the parse functions that describe the format of the input
- made the parse-grid function more concise by using the 'map' list abstraction as indicated in feedback 
|#

;------------------------------------------------------------------------------------------------------------------------------------------------------------

#| After discussing, we decided to go with Ahan's group assignment, because the structure of the functions and helper functions,
 along with the efficiency of the code seemed to be better.
- The data definitions were better defined with more signatures, a better range of examples, and overall more concise.
- Moreover, the cell->image function was more structured with appropriate delegation to helper functions
- And didn't require as many arithmetic operations (and both of us thought the visual representation was better, although not a deciding factor).

 Break-down by exercise:
- Exercise 1: The logic is much simpler with the use of member? and a recursive call
  in comparison to the coutning of duplicates in the main function, removing them in the first helper and evaluating multi-set
  equality in the third.

- Exercise 2: The definition of a grid is much more efficient than assigning a position to each cell and having a grid be
  a long list of multiple cells. Ahan's definition has a list with sublists of cells in it, which makes for better indexing of cells
  and representation of a row of cells.

- Exercise 3: grid->image in Ahan's version uses the functions 'above' and 'beside', which reduced complexity, and made the code significantly more efficient.

- Exercise 4: Because of the composition of the grid as a list of sublists, accessing each element for get-cell (and for set-cell) was done with
  the 'list-ref' function, which was a pretty creative implementation.

- Exercise 5: This approach seemed somewhat unorthodox, but after understanding it, it looked like a unique way of going about it, especially with the
  absence of the function in this language that can extract the index of an element, and the fact that we did not have a position as a parameter for each cell anymore.

- Exercise 6: The implementation of get-neighbors was much more efficient than Ami's version. Checking whether a posn was in the grid was tailored to our own definitions,
  but the filtering of neighbors was less complicated in Ahan's version. He sends all possible cardinal neighbors and filters the invalid ones using the valid-posn? function,
  whereas Ami's version compares a position of a cell with positions of all other cells and does so using some "complex" arithmetic. |#


;;;;;;;;;;;;;;;;;;;;;;

;; DATA DEFINITIONS ;;

;;;;;;;;;;;;;;;;;;;;;;


; A Cell is one of:
; An input/output wire
; A gate, which can be an AND, OR or NOT gate
; A conductive plate
; or empty

(define-struct iowire (NAME-WIRE TYPE-WIRE CHARGE))
(define-struct gates (TYPE-GATE DIRECTION CHARGE))
(define-struct cp (CONDUCTIVE-PLATE CHARGE))
(define EMPTY "empty")


; signatures for a Cell and its types
(define ACharge (signature (enum #t #f "null")))
(define AnIO-Wire (signature (IowireOf String String ACharge)))
(define AGate (signature (GatesOf String String ACharge)))
(define AnEmpty (signature (enum EMPTY)))
(define Acp (signature (CpOf String ACharge)))
(define ACell (signature (mixed AnIO-Wire AGate AnEmpty Acp )))


; Interpretation:
; these can be used in a game where players use gates from boolean logic and solve puzzles on grids
; containing input wires, output wires, conducting plates, etc.

; A type-gate is:
;AND
;OR
;NOT
;Interpretation: the different types of gates in a logic circuit
;Examples:
(define AND "AND")
(define OR "OR")
(define NOT "NOT")


; A direction is one of:
; - UP
; - DOWN
; - LEFT
; - RIGHT
;Interpretation: The different directions that a logic gate or conducting plate can have.
;Examples
(define UP "UP")
(define DOWN "DOWN")
(define LEFT "LEFT")
(define RIGHT "RIGHT")
(define ADirection (signature (enum UP DOWN LEFT RIGHT)))

; A name_wire is a string which contains the name of the wire

; A type-wire is:
; Input Wire
; Output Wire

;Interpretation: To represent the type of IO wire being used.
;Examples:
(define INPUT-WIRE "INPUT")
(define OUTPUT-WIRE "OUTPUT")

;templates for a cell and some of its types
(: iowire-temp (AnIO-Wire -> Any))
(define (iowire-temp str)
  (cond
    [(string=? INPUT-WIRE (iowire-NAME-WIRE str)) (... (iowire-NAME-WIRE str) ... (iowire-TYPE-WIRE str) ... (iowire-CHARGE str))]
    [(string=? OUTPUT-WIRE (iowire-NAME-WIRE str))( ... (iowire-NAME-WIRE str) ... (iowire-TYPE-WIRE str) ... (iowire-CHARGE str))]))

(define (cp-temp str)
  (... (cp-CONDUCTIVE-PLATE str) ... (cp-CHARGE str)))
    

(: gates-temp (AGate -> Any))
(define (gates-temp str)
  (cond
    [(string=? (gates-TYPE-GATE str) AND) (... (gates-TYPE-GATE str)...(gates-DIRECTION str) ... (gates-CHARGE str))]
    [(string=? (gates-TYPE-GATE str) OR) (... (gates-TYPE-GATE str)...(gates-DIRECTION str) ... (gates-CHARGE str))]
    [(string=? (gates-TYPE-GATE str) NOT) (... (gates-TYPE-GATE str)...(gates-DIRECTION str) ... (gates-CHARGE str))]))



(: cell-temp (ACell -> Any))
(define (cell-temp cell)
  (cond [(iowire? cell)...]
        [(cp? cell)...]
        [(string=? EMPTY cell)...]
        [(gates? cell)...]))

;Examples of cells
(define C1 (make-iowire "X" INPUT-WIRE #t))
(define C2 (make-iowire "OUT" OUTPUT-WIRE #f))
(define C3 (make-gates OR DOWN #t))
(define C4 (make-gates AND LEFT #t))
(define C5 (make-gates NOT UP #f))
(define C6 (make-cp "C" #t))
(define C7 EMPTY)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; DEFINTION OF A LIST OF CELLS ;;
;;   IN CONTEXT OF MULTI-SETS   ;;

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; A [ListOf Cell] is one of:
; - '()
; - (cons Cell [ListOf Cell])
; Interpretation: a sequence of Cells

(define (list-of-cell-temp l) 
  (... (cond [(empty? l) ...]
             [(cons? l) ... (cell-temp (first l))
                        ... (list-of-cell-temp (rest l))])))

;Examples of ListOf Cell
(define L1 (list C1 C2 C6 C7))
(define L2 (list C2 C3 C1 C7))
(define L3 (list C2 C4 C6))
(define L4 (list C2 C6 C4))
(define L5 (list C1 C7 C3 C2))
(define L6 (list C3 C7 C1))
(define L7 (list C6 C4 C6))
(define L8 (list C4 C2 C7 C3))
(define L9 (list C1 C1 C6 C5))




; Exercise 1 -----------------------------------------------------------------------------------------
(: cell-set-equal? ((ListOf ACell) (ListOf ACell) -> Boolean))
;checks if the two list of Cells are equal by multi-set equality
(define (cell-set-equal? loc1 loc2)
  (and
   (= (length loc1) (length loc2))
   (contains-cell? loc1 loc2)))

(check-expect (cell-set-equal? L1 L2) #false)
(check-expect (cell-set-equal? '() '())#true)
(check-expect (cell-set-equal? L3 L4)#true)
(check-expect (cell-set-equal? L5 L6)#false)
(check-expect (cell-set-equal? '() L7)#false)




(: contains-cell? ([ListOf ACell] [ListOf ACell] -> Boolean))
; evaluates multi-set equality between two list of Cells
(define (contains-cell? loc1 loc2)
  (cond
    [(empty? loc1) #true]
    [(cons? loc1) (if (member? (first loc1) loc2) (contains-cell? (rest loc1) loc2) #false)]))

(check-expect (contains-cell? L1 L2) #false)
(check-expect (contains-cell? '() '())#true)
(check-expect (contains-cell? L3 L4)#true)
(check-expect (contains-cell? L5 L6)#false)



;;;;;;;;;;;;;;;;;;;;;;;;;;

;; DEFINITION OF A GRID ;;

;;;;;;;;;;;;;;;;;;;;;;;;;;

; Exercise 2 -----------------------------------------------------------------------------------------
 
; A Grid is a [ListOf [ListOf Cell]]
(define AGrid (signature [ListOf [ListOf ACell]])) 
; Each sublist in Grid represents a row of Cells in the grid.
; (x,y) coordinates begin at 0,0 in the top left.
; The row number (each sublist) represents the y coordinate,
; and the column number (each item in sublists) represents the x coordinate.

;Examples
(define G1 (list L1 L5 L8 L9))
(define G2 (list L3 L4 L6))


(define (grid-temp g)
  (... (cond [(empty? g) ...]
             [(cons? g) (... (list-of-cell-temp (first  g))
                             ... (grid-temp (rest g)))])))




;;;;;;;;;;;;;;;;;;;;;;;

;; DRAWING FUNCTIONS ;;

;;;;;;;;;;;;;;;;;;;;;;;

; Exercise 3 -----------------------------------------------------------------------------------------

;CELL-SIZE
;Stores the size of a Cell in a grid in pixels
(define CELL-SIZE 100)


(: grid->image (AGrid -> Image))
; Converts a Grid into an Image
(define (grid->image grid)
  (cond
    [(empty? grid) (empty-scene 0 0)]
    [(cons? grid) (above (draw-row (first grid))
                         (grid->image (rest grid)))]))

(check-expect (grid->image G1) (above (draw-row (first G1))
                                      (draw-row (second G1))
                                      (draw-row (third G1))
                                      (draw-row (fourth G1))))

(check-expect (grid->image G2) (above (draw-row (first G2))
                                      (draw-row (second G2))
                                      (draw-row (third G2))))



(: draw-row ([ListOf ACell] -> Image))
; takes in each row of a Grid and converts it into an image
(define (draw-row r)
  (cond
    [(empty? r) (empty-scene 0 0)]
    [(cons? r) (beside (overlay (cell->image (first r)) (charge-of-cell (first r))) (draw-row (rest r)))]))

(check-expect (draw-row L5) (beside (overlay (cell->image (first L5)) (square (+ 10 CELL-SIZE) "solid" "red"))
                                    (overlay (cell->image (second L5)) (square (+ 10 CELL-SIZE) "solid" "black"))
                                    (overlay (cell->image (third L5)) (square (+ 10 CELL-SIZE) "solid" "red"))
                                    (overlay (cell->image (fourth L5)) (square (+ 10 CELL-SIZE) "solid" "blue"))))

(check-expect (draw-row L7) (beside (overlay (cell->image (first L7)) (square (+ 10 CELL-SIZE) "solid" "red"))
                                    (overlay (cell->image (second L7)) (square (+ 10 CELL-SIZE) "solid" "red"))
                                    (overlay (cell->image (third L7)) (square (+ 10 CELL-SIZE) "solid" "red"))))


; Red borders represents positive charge. 
; Blue borders represents negative charge.
; Black borders represents no charge.


(: charge-of-cell (ACell -> Image))
; returns a visual representation of the cell with a charge
(define (charge-of-cell cell)
  (cond 
    [(cp? cell) (cond
                  [(string? (cp-CHARGE cell)) (square (+ 10 CELL-SIZE) "solid" "black")]
                  [(boolean? (cp-CHARGE cell)) (if (false? (cp-CHARGE cell)) (square (+ 10 CELL-SIZE) "solid" "blue")
                                                   (square (+ 10 CELL-SIZE) "solid" "red"))])]
                     
    [(and (string? cell) (string=? cell "empty")) (square (+ 10 CELL-SIZE) "solid" "black")]
        
    [(and (iowire? cell) (string=? (iowire-TYPE-WIRE cell) "OUTPUT")) (cond
                                                                        [(string? (iowire-CHARGE cell)) (square (+ 10 CELL-SIZE) "solid" "black")]
                                                                        [(boolean? (iowire-CHARGE cell)) (if (false? (iowire-CHARGE cell)) (square (+ 10 CELL-SIZE) "solid" "blue")
                                                                                                             (square (+ 10 CELL-SIZE) "solid" "red"))])]
    [(and (iowire? cell) (string=? (iowire-TYPE-WIRE cell) "INPUT")) (cond
                                                                       [(string? (iowire-CHARGE cell)) (square (+ 10 CELL-SIZE) "solid" "black")]
                                                                       [(boolean? (iowire-CHARGE cell)) (if (false? (iowire-CHARGE cell)) (square (+ 10 CELL-SIZE) "solid" "blue")
                                                                                                            (square (+ 10 CELL-SIZE) "solid" "red"))])]
    [(gates? cell) (cond
                     [(string? (gates-CHARGE cell)) (square (+ 10 CELL-SIZE) "solid" "black")]
                     [(boolean? (gates-CHARGE cell)) (if (false? (gates-CHARGE cell)) (square (+ 10 CELL-SIZE) "solid" "blue")
                                                         (square (+ 10 CELL-SIZE) "solid" "red"))])]))

(check-expect (charge-of-cell C1) (square (+ 10 CELL-SIZE) "solid" "red"))
(check-expect (charge-of-cell C2) (square (+ 10 CELL-SIZE) "solid" "blue"))
(check-expect (charge-of-cell C3) (square (+ 10 CELL-SIZE) "solid" "red"))
(check-expect (charge-of-cell C4) (square (+ 10 CELL-SIZE) "solid" "red"))
(check-expect (charge-of-cell C5) (square (+ 10 CELL-SIZE) "solid" "blue"))


;"GREEN" represents input wires. 
;"ORANGE" represents output wires. 
;"YELLOW" (with a caution sign) represents conductive plate. 
;"WHITE" represents empty.
;"BROWN" represents gate and the triangles represent the orientation of the gate


(: cell->image (ACell -> Image))
; converts a Cell into an image
(define (cell->image Cell)
  (cond [(iowire? Cell)
         (draw-iowire Cell)]

        [(cp? Cell) (place-image (text "!" 70 "darkslategray")
                                 48 62
        
                                 (place-image (triangle 80 "outline" "darkslategray")
                                              50 50
                                              (place-image (rectangle 300 300 "solid" "yellow")
                                                           0 0
                                                                           
                                                           (empty-scene CELL-SIZE CELL-SIZE))))]


        [(and (string? Cell) (string=? Cell EMPTY)) (place-image (rectangle 200 200 "solid" "white")
                                                                 0 0
                                                                           
                                                                 (empty-scene CELL-SIZE CELL-SIZE))]
        
        [(gates? Cell)
         (cond

           [(string=? (gates-TYPE-GATE Cell) AND) (place-image (text (gates-TYPE-GATE Cell) 25 "white")
                                                               30 50
                                                               (place-image (rotate (ang (gates-DIRECTION Cell)) (isosceles-triangle 30 25 "solid" "white"))
                                                                            80 47
                                                                                         
                                                                            (place-image (rectangle 300 300 "solid" "brown")
                                                                                         0 0
                                                                           
                                                                                         (empty-scene CELL-SIZE CELL-SIZE))))]
        

           [(string=? (gates-TYPE-GATE Cell) OR) (place-image (text (gates-TYPE-GATE Cell) 25 "white")
                                                              30 50 
                                                              (place-image (rotate (ang (gates-DIRECTION Cell)) (isosceles-triangle 30 25 "solid" "white"))
                                                                           80 47
                                                                                         
                                                                           (place-image (rectangle 300 300 "solid" "brown")
                                                                                        0 0
                                                                           
                                                                                        (empty-scene CELL-SIZE CELL-SIZE))))]
        
              

           [(string=? (gates-TYPE-GATE Cell) NOT) (place-image (text (gates-TYPE-GATE Cell) 25 "white")
                                                               30 50 
                                                               (place-image (rotate (ang (gates-DIRECTION Cell)) (isosceles-triangle 30 25 "solid" "white"))
                                                                            80 47
                                                                                         
                                                                            (place-image (rectangle 300 300 "solid" "brown")
                                                                                         0 0
                                                                           
                                                                                         (empty-scene CELL-SIZE CELL-SIZE))))])]))

(check-expect (cell->image C1) (place-image (text (iowire-NAME-WIRE C1) 25 "white")
                                            50 50
                                            (place-image (rectangle 300 300 "solid" "green")
                                                         0 0
                                                                           
                                                         (empty-scene CELL-SIZE CELL-SIZE))))
(check-expect (cell->image C2) (place-image (text (iowire-NAME-WIRE C2) 25 "white")
                                            50 50
                                            (place-image (rectangle 300 300 "solid" "orange")
                                                         0 0
                                                                           
                                                         (empty-scene CELL-SIZE CELL-SIZE))))
(check-expect (cell->image C3) (place-image (text (gates-TYPE-GATE C3) 25 "white")
                                            30 50 
                                            (place-image (rotate 180 (isosceles-triangle 30 25 "solid" "white"))
                                                         80 47
                                                                                         
                                                         (place-image (rectangle 300 300 "solid" "brown")
                                                                      0 0
                                                                           
                                                                      (empty-scene CELL-SIZE CELL-SIZE)))))
(check-expect (cell->image C4) (place-image (text (gates-TYPE-GATE C4) 25 "white")
                                            30 50 
                                            (place-image (rotate 90 (isosceles-triangle 30 25 "solid" "white"))
                                                         80 47
                                                                                         
                                                         (place-image (rectangle 300 300 "solid" "brown")
                                                                      0 0
                                                                           
                                                                      (empty-scene CELL-SIZE CELL-SIZE)))))
(check-expect (cell->image C5) (place-image (text (gates-TYPE-GATE C5) 25 "white")
                                            30 50
                                            (place-image (rotate 0 (isosceles-triangle 30 25 "solid" "white"))
                                                         80 47
                                                                                         
                                                         (place-image (rectangle 300 300 "solid" "brown")
                                                                      0 0
                                                                           
                                                                      (empty-scene CELL-SIZE CELL-SIZE)))))
(check-expect (cell->image C6) (place-image (text "!" 70 "darkslategray")
                                            48 62
        
                                            (place-image (triangle 80 "outline" "darkslategray")
                                                         50 50 
                                                         (place-image (rectangle 300 300 "solid" "yellow")
                                                                      0 0
                                                                           
                                                                      (empty-scene CELL-SIZE CELL-SIZE)))))
(check-expect (cell->image C7) (place-image (rectangle 300 300 "solid" "white")
                                            0 0
                                                                           
                                            (empty-scene CELL-SIZE CELL-SIZE)))


(: ang (ADirection -> Integer))
(define (ang direction)
  ; determines the angle the arrow, represented in the gates cell, needs to be rotated
  (cond [(string=? direction UP)  0]
        [(string=? direction LEFT)  90]
        [(string=? direction RIGHT)  -90]
        [(string=? direction DOWN)  180]))

(check-expect (ang UP) 0)
(check-expect (ang DOWN) 180)
(check-expect (ang RIGHT) -90)
(check-expect (ang LEFT) 90)



(: draw-iowire (AnIO-Wire -> Image))
; draws an IOWire
(define (draw-iowire IO)
  (cond [(string=? (iowire-TYPE-WIRE IO) INPUT-WIRE ) (place-image (text (iowire-NAME-WIRE IO) 25 "white")
                                                                   50 50
                                                                   (place-image (rectangle 300 300 "solid" "green")
                                                                                0 0
                                                                           
                                                                                (empty-scene CELL-SIZE CELL-SIZE)))]
        [(string=? (iowire-TYPE-WIRE IO) OUTPUT-WIRE ) (place-image (text (iowire-NAME-WIRE IO) 25 "white")
                                                                    50 50
                                                                    (place-image (rectangle 300 300 "solid" "orange")
                                                                                 0 0
                                                                           
                                                                                 (empty-scene CELL-SIZE CELL-SIZE)))]))

(check-expect (draw-iowire C1) (place-image (text (iowire-NAME-WIRE C1) 25 "white")
                                            50 50 
                                            (place-image (rectangle 300 300 "solid" "green")
                                                         0 0
                                                                           
                                                         (empty-scene CELL-SIZE CELL-SIZE))))
(check-expect (draw-iowire C2) (place-image (text (iowire-NAME-WIRE C2) 25 "white")
                                            50 50 
                                            (place-image (rectangle 300 300 "solid" "orange")
                                                         0 0
                                                                           
                                                         (empty-scene CELL-SIZE CELL-SIZE))))


;;;;;;;;;;;;;;;;;;;;;;;;;;

;; ACCESSOR AND MUTATOR ;;
;; FUNCTIONS FOR CELLS  ;;

;;;;;;;;;;;;;;;;;;;;;;;;;;
     
; EXERCISE 4----------------------------------------------------------------------------------------

;DATA DEFINTIION
(define ACellOrFalse (signature (mixed ACell False)))
; a Cell or False

(define ACF1 C2)
(define ACF2 #false)

(: get-cell (AGrid APosn -> ACellOrFalse))
;takes a Grid and a Posn and returns the Cell at the given position in the given Grid
(define (get-cell g p)
  (cond [(not (valid-posn? g p)) #false]
        [else (list-ref (list-ref g (posn-y p)) (posn-x p))]))

(check-expect (get-cell G1 (make-posn 2 1)) C3)
(check-expect (get-cell G2 (make-posn 3 4)) #false)
(check-expect (get-cell G2 (make-posn 0 0)) C2) 

; EXERCISE 5----------------------------------------------------------------------------------------

(define AGridOrFalse (signature (mixed AGrid False)))
; A Grid or False

(define AGF1 G1)
(define AGF2 #false)


(: set-cell (AGrid APosn ACell -> AGridOrFalse))
; takes a Grid, a Posn and a new Cell, and returns a new Grid where the given Posn has been replaced with the new Cell.
(define (set-cell g p c)
  (cond
    [(not (valid-posn? g p)) #false]
    [else (change-grid g (- (length g) (posn-y p)) (- (length (first g)) (posn-x p)) c)]))

(check-expect (set-cell G1 (make-posn 4 8) C7) #false)
(check-expect (set-cell G1 (make-posn 0 2) C3) (list L1 L5 (list C3 C2 C7 C3) L9))
(check-expect (set-cell G2 (make-posn 1 1) C5) (list L3 (list C2 C5 C4) L6))


(define (change-grid g y x c)
  ; returns a grid with the new cell in place of the old cell
  (cond
    [(empty? g) '()]
    [(cons? g) (if (= (length g) y) (cons (change-cell (first g) x c) (change-grid (rest g) y x c)) (cons (first g) (change-grid (rest g) y x c)))]))

(check-expect (change-grid G1 2 2 C6) (list L1 L5 (list C4 C2 C6 C3) L9))
(check-expect (change-grid G2 1 3 C7) (list L3 L4 (list C7 C7 C1)))
(check-expect (change-grid '() 1 1 C2) '())


(: change-cell ([ListOf ACell] Number ACell -> [ListOf ACell]))
; returns a list of Cell with the new Cell in place of the Cell that was at the given Posn
(define (change-cell loc x c)
  (cond
    [(empty? loc) '()]
    [(cons? loc) (if (= (length loc) x)
                     (cons c (change-cell (rest loc) x c))
                     (cons (first loc) (change-cell (rest loc) x c)))]))

(check-expect (change-cell L2 2 C6) (list C2 C3 C6 C7))
(check-expect (change-cell '() 2 C6) '())

;;;;;;;;;;;;;;;;;;;;;;l

;; CARDINAL NEIGHBOR ;;
;;     FUNCTIONS     ;;

;;;;;;;;;;;;;;;;;;;;;;;

     
; EXERCISE 6----------------------------------------------------------------------------------------
    

; Creates a list of four posn's, represents each potential cardinal neighbor
; Then calls helper function, passing list and grid. Iterates through list of posns,
; determines if they are valid, and if not, they get skipped over/removed. Then,

(: valid-posn? (AGrid APosn -> Boolean))
(define (valid-posn? g p)
  ;checks whether given posn is valid in a grid or not
  (not (or (>= (posn-y p) (length g))
           (>= (posn-x p) (length (first g)))
           (< (posn-y p) 0)
           (< (posn-x p) 0))))

(check-expect (valid-posn? G1 (make-posn 0 0)) #true)
(check-expect (valid-posn? G1 (make-posn 3 4)) #false)
(check-expect (valid-posn? G2 (make-posn 1 2)) #true)
(check-expect (valid-posn? G2 (make-posn -2 3)) #false)

(: get-neighbors (AGrid APosn -> (mixed False [ListOf ACell])))
(define (get-neighbors g p)
  ; creates a list of every possible cardinal neighbour of the posn, regardless of it being invalid
  ; then, it sends it to a function valid-neighbours to remove the incorrect ones
  (cond [(not (valid-posn? g p)) #false]
        [else (valid-neighbors g (list
                                  (make-posn (add1 (posn-x p)) (posn-y p))
                                  (make-posn (posn-x p) (add1 (posn-y p)))
                                  (make-posn (sub1 (posn-x p)) (posn-y p))
                                  (make-posn (posn-x p) (sub1 (posn-y p)))))]))

(check-expect (get-neighbors G1 (make-posn 0 -1)) #false)
(check-expect (get-neighbors G2 (make-posn 3 4)) #false)

(: valid-neighbors (AGrid [ListOf APosn] -> [ListOf ACell]))
(define (valid-neighbors g lop)
  ; filters the invalid cardinal neighbours from the list of posn's, and converts the rest into Cells
  (cond [(empty? lop) '()]
        [(cons? lop) (if (valid-posn? g (first lop))
                         (cons (get-cell g (first lop)) (valid-neighbors g (rest lop)))
                         (valid-neighbors g (rest lop)))]))

(check-expect (valid-neighbors G1 (list (make-posn 1 0) (make-posn 0 1) (make-posn -1 0) (make-posn 0 -1))) (list C2 C1))
(check-expect (valid-neighbors G2 (list (make-posn 2 1) (make-posn 1 2) (make-posn 0 1) (make-posn 1 0))) (list C4 C7 C2 C4))
(check-expect (valid-neighbors G1  '()) '())



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;                                                                                          ;
;------------------------------------------- 6B -------------------------------------------;
;                                                                                          ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


;Exercise 2---------------------------------------------------------------------------------------------------------
(define SExp (signature (mixed Number String Boolean Symbol [ListOf SExp])))

(: gate? (String -> Boolean))
;checks whether input is one of the three gates
(define (gate? g)
  (or (string=? g AND)
      (string=? g OR)
      (string=? g NOT)))

(check-expect (gate? OR) #t)
(check-expect (gate? AND) #t)
(check-expect (gate? NOT) #t)
(check-expect (gate? OUTPUT-WIRE) #f)

(: direction? (String -> Boolean))
;checks whether input is one of the cardinal directions
(define (direction? d)
  (or (string=? d UP)
      (string=? d DOWN)
      (string=? d LEFT)
      (string=? d RIGHT)))

(check-expect (direction? UP) #t)
(check-expect (direction? LEFT) #t)
(check-expect (direction? RIGHT) #t)
(check-expect (direction? DOWN) #t)
(check-expect (direction? NOT) #f)
(check-expect (direction? "direction") #f)

(: io? (String -> Boolean))
;checks whether input is an input or output wire
(define (io? i)
  (or (string=? i INPUT-WIRE)
      (string=? i OUTPUT-WIRE)))

(check-expect (io? INPUT-WIRE) #t)
(check-expect (io? OUTPUT-WIRE) #t)
(check-expect (io? RIGHT) #f)
(check-expect (io? "x") #f)

(: charge? ((mixed String Boolean) -> Boolean))
;checks whether input is one of the charges (true false or null)
(define (charge? c)
  (or (boolean? c)
      (string=? c "null")))

(check-expect (charge? "no charge") #f)
(check-expect (charge? "null") #t)
(check-expect (charge? #t) #t)
(check-expect (charge? #f) #t)


;;;;;;;;;;;;;;;;;;;;;;

;; PARSE FUNCTIONS ;;

;;;;;;;;;;;;;;;;;;;;;;

(: parse-cell (SExp -> ACell))
; Parses an SExp of a Cell
; Acceptable inputs can be a string or a list.
; For strings, the only valid string is the string "empty"
; For lists, there are three types of valid lists:
; - The first one is for input/output, which would be in the form:
; (list name-of-wire type-of-wire 'iowire charge-of-wire)

; - The second one is for gates, which would be in the form:
; (list type-of-gate directionof-gate 'gates charge-of-wire)

; - The third one is for conductive plates, which would be in the form:
; (list name-of-conductive-plate charge-of-conductive-plate)

; Note: Except input wires, all other cells would have a starting charge of "null"


(define (parse-cell sexpr)
  (cond [(number? sexpr) (error "Invalid Number")]
        [(string? sexpr) (cond [(string=? sexpr EMPTY) EMPTY]
                              
                               [else (error "Invalid String")])]
        [(boolean? sexpr) (error "Invalid Bool")]
        [(symbol? sexpr) (error "Invalid Sym")]
        [(list? sexpr) (cond [(and (>= (length sexpr) 3)
                                   (string? (first sexpr))
                                   (string? (second sexpr))
                                   (symbol? (third sexpr)))
                              (cond [(and (io? (second sexpr))
                                          (symbol=? (third sexpr) 'iowire)) (make-iowire (first sexpr) (second sexpr) (fourth sexpr))]
                                    [(and (gate? (first sexpr))
                                          (direction? (second sexpr))
                                          (symbol=? (third sexpr) 'gates)) (make-gates (first sexpr) (second sexpr) (fourth sexpr))])]
                             [(and (string? (first sexpr)) (charge? (second sexpr))) (make-cp (first sexpr) (second sexpr))]
                             [else (error "Invalid List")])]))

(define S1 "empty")
(define S2 '("C" "null"))
(define S3 4)
(define S4 #t)
(define S5 'gates)
(define S6 `("INP" ,INPUT-WIRE iowire #t))
(define S6-null `("INP" ,INPUT-WIRE iowire "null"))
(define S6_1+ `("INP1" ,INPUT-WIRE iowire #t))
(define S6_1- `("INP1" ,INPUT-WIRE iowire #f))
(define S6_2+ `("INP2" ,INPUT-WIRE iowire #t))
(define S6_2- `("INP2" ,INPUT-WIRE iowire #f))
(define S6_1_null `("INP1" ,INPUT-WIRE iowire "null"))
(define S6_2_null `("INP2" ,INPUT-WIRE iowire "null"))
(define S7 `("OUT" ,OUTPUT-WIRE iowire "null"))
(define S7_1 `("OUT1" ,OUTPUT-WIRE iowire "null"))
(define S7_2 `("OUT2" ,OUTPUT-WIRE iowire "null"))
(define S8 `(,AND ,UP gates "null"))
(define S9 `(,OR ,UP gates "null"))
(define S10 `(,NOT ,UP gates "null"))
(define S11 `(,AND ,DOWN gates "null"))
(define S12 `(,OR ,DOWN gates "null"))
(define S13 `(,NOT ,DOWN gates "null"))
(define S14 `(,AND ,LEFT gates "null"))
(define S15 `(,OR ,LEFT gates "null"))
(define S16 `(,NOT ,LEFT gates "null"))
(define S17 `(,AND ,RIGHT gates "null"))
(define S18 `(,OR ,RIGHT gates "null"))
(define S19 `(,NOT ,RIGHT gates "null"))
(check-expect (parse-cell S1) "empty")
(check-expect (parse-cell S2) (make-cp "C" "null"))
(check-error (parse-cell S3))
(check-error (parse-cell S4))
(check-error (parse-cell S5))
(check-expect (parse-cell S6) (make-iowire "INP" "INPUT" #true))
(check-expect (parse-cell S6_1-) (make-iowire "INP1" "INPUT" #false))
(check-expect (parse-cell S6_1+) (make-iowire "INP1" "INPUT" #true))
(check-expect (parse-cell S6_2-) (make-iowire "INP2" "INPUT" #false))
(check-expect (parse-cell S6_2+) (make-iowire "INP2" "INPUT" #true))
(check-expect (parse-cell S7) (make-iowire "OUT" "OUTPUT" "null"))
(check-expect (parse-cell S7_1) (make-iowire "OUT1" "OUTPUT" "null"))
(check-expect (parse-cell S7_2) (make-iowire "OUT2" "OUTPUT" "null"))
(check-expect (parse-cell S8) (make-gates "AND" "UP" "null"))
(check-expect (parse-cell S9) (make-gates "OR" "UP" "null"))
(check-expect (parse-cell S10) (make-gates "NOT" "UP" "null"))
(check-expect (parse-cell S11) (make-gates "AND" "DOWN" "null"))
(check-expect (parse-cell S12) (make-gates "OR" "DOWN" "null"))
(check-expect (parse-cell S13) (make-gates "NOT" "DOWN" "null"))
(check-expect (parse-cell S14) (make-gates "AND" "LEFT" "null"))
(check-expect (parse-cell S15) (make-gates "OR" "LEFT" "null"))
(check-expect (parse-cell S16) (make-gates "NOT" "LEFT" "null"))
(check-expect (parse-cell S17) (make-gates "AND" "RIGHT" "null"))
(check-expect (parse-cell S18) (make-gates "OR" "RIGHT" "null"))
(check-expect (parse-cell S19) (make-gates "NOT" "RIGHT" "null"))

;Exercise 3---------------------------------------------------------------------------------------------------------
(: parse-grid (SExp -> AGrid))
;Parses an SExp of a Grid
; The format for the SExpr of a Grid is a (list (list SExp1 SExp2 SExp3 ....)
;                                               (list SExp1 SExp2 SExp3 ....)
;                                               (list SExp1 SExp2 SExp3 ....))
; Which is a 2-dimensional list, where the main list consists of an arbitrary amount of sublists.
; Each sublist represents a row of the grid and each row has some arbitrary amount of S-Expressions of a cell
; (subject to the limitations of what classifies as a valid S-Expression of a cell)

; Note: The grid MUST be a square or a rectangle, i.e. there can be any amount of rows but the number of cells
; in each sublist (the number of columns) must be the same.

(define (parse-grid sexpr)
  (if (list? sexpr) (map (lambda (row) (parse-row row)) sexpr)
      (error "Invalid")))

(define R1 `(,S2 ,S6_1+ ,S12 ,S6_2- ,S2 ,S2 ,S2))
(define R2 `(,S2 ,S1 ,S1 ,S1 ,S1 ,S1 ,S2))
(define R3 `(,S2 ,S1 ,S19 ,S2 ,S7 ,S1 ,S2 ))
(define R4 `(,S2 ,S1 ,S2 ,S1 ,S1 ,S1 ,S2))
(define R5 `(,S2 ,S2 ,S8 ,S2 ,S2 ,S2 ,S2 ))

(define Grid1 (list R1 R2 R3 R4 R5))
(check-expect (parse-grid Grid1) (list (parse-row R1) (parse-row R2) (parse-row R3) (parse-row R4) (parse-row R5)))

(: parse-row (SExp -> [ListOf ACell]))
;Parses an SExp of a row of a Grid
; The format for the SExpr of a row of a Grid is a (list SExp1 SExp2 SExp3 ....)
; Each row consists of some arbitrary amount of S-Expressions of a cell.
; (subject to the limitations of what classifies as a valid S-Expression of a cell)

(define (parse-row sexpr2)
  (cond
    [(empty? sexpr2) '()]
    [(list? sexpr2) (map parse-cell sexpr2)]))



(check-expect (parse-row R1) (list (make-cp "C" "null") (make-iowire "INP1" "INPUT" #t) (make-gates "OR" "DOWN" "null") (make-iowire "INP2" "INPUT" #false) (make-cp "C" "null") (make-cp "C" "null") (make-cp "C" "null")))

(check-expect (parse-row R2) (list (make-cp "C" "null") "empty" "empty" "empty" "empty" "empty" (make-cp "C" "null") ))

(check-expect (parse-row R3) (list  (make-cp "C" "null") "empty" (make-gates "NOT" "RIGHT" "null") (make-cp "C" "null") (make-iowire "OUT" "OUTPUT" "null") "empty" (make-cp "C" "null") ))

(check-expect (parse-row R4) (list (make-cp "C" "null") "empty" (make-cp "C" "null")  "empty" "empty" "empty" (make-cp "C" "null") ))

(check-expect (parse-row R5) (list  (make-cp "C" "null" ) (make-cp "C" "null" )  (make-gates "AND" "UP" "null") (make-cp "C" "null") (make-cp "C" "null") (make-cp "C" "null") (make-cp "C" "null")) )



;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; UPDATE GRID FUNCTIONS ;;

;;;;;;;;;;;;;;;;;;;;;;;;;;;

   
;Exercise 4---------------------------------------------------------------------------------------------------------
;(: update-grid (AGrid -> AGrid))
; updates the grid step by step
(define (update-grid ws)
  (upgrade-grid ws ws (make-posn 0 0)))

(define (upgrade-grid grid grid2 pos)
  ; helper function for update-grid to send a posn in
  (cond
    [(not (valid-posn? grid2 pos)) '()]
    [(list? grid) (cons (charge-row grid grid2 (first grid) pos)
                        (upgrade-grid (rest grid) grid2 (make-posn (posn-x pos) (add1 (posn-y pos)))))]))

; update-grid would return the same outputs as upgrade-grid
(check-expect (upgrade-grid (parse-grid Grid1) (parse-grid Grid1) (make-posn 0 0)) (list (charge-row (parse-grid Grid1) (parse-grid Grid1) (parse-row (first Grid1)) (make-posn 0 0))
                                                                                         (charge-row (parse-grid Grid1) (parse-grid Grid1) (parse-row (second Grid1)) (make-posn 0 1))
                                                                                         (charge-row (parse-grid Grid1) (parse-grid Grid1) (parse-row (third Grid1)) (make-posn 0 2))
                                                                                         (charge-row (parse-grid Grid1) (parse-grid Grid1) (parse-row (fourth Grid1)) (make-posn 0 3))
                                                                                         (charge-row (parse-grid Grid1) (parse-grid Grid1) (parse-row (fifth Grid1)) (make-posn 0 4))))
    

(: charge-row ( [ListOf [ListOf ACell]] [ListOf [ListOf ACell]] [ListOf ACell] APosn -> [ListOf ACell]))
; returns each row's charge with the propogated charge
(define (charge-row grid grid2 row pos)
  (cond 
    [(not (valid-posn? grid2 pos)) '()]
    [(cp? (first row)) (cons (cp-charge grid grid2 (first row) pos) (charge-row grid grid2 (rest row) (make-posn (add1 (posn-x pos)) (posn-y pos))))]
    [(and (string? (first row)) (string=? (first row) "empty"))(cons "empty" (charge-row grid grid2 (rest row) (make-posn (add1 (posn-x pos)) (posn-y pos))))]
    [(and (iowire? (first row)) (string=? (iowire-TYPE-WIRE (first row)) "OUTPUT"))(cons (output-charge grid grid2 (first row) pos)
                                                                                         (charge-row grid grid2 (rest row) (make-posn (add1 (posn-x pos)) (posn-y pos))))]
    [(and (iowire? (first row)) (string=? (iowire-TYPE-WIRE (first row)) "INPUT"))(cons (first row)
                                                                                        (charge-row grid grid2 (rest row) (make-posn (add1 (posn-x pos)) (posn-y pos))))]
    [(gates? (first row)) (cons (gates-charge grid grid2 (first row) pos) (charge-row grid grid2 (rest row) (make-posn (add1 (posn-x pos)) (posn-y pos))))])) 

(check-expect (charge-row (parse-grid Grid1) (parse-grid Grid1) (parse-row (first Grid1)) (make-posn 0 0)) (list (make-cp "C" #true)
                                                                                                                 (make-iowire "INP1" "INPUT" #true)
                                                                                                                 (make-gates "OR" "DOWN" #true)
                                                                                                                 (make-iowire "INP2" "INPUT" #false)
                                                                                                                 (make-cp "C" #false)
                                                                                                                 (make-cp "C" "null")
                                                                                                                 (make-cp "C" "null")))

(check-expect (charge-row (parse-grid Grid1) (parse-grid Grid1) (parse-row (second Grid1)) (make-posn 0 1)) (list
                                                                                                             (make-cp "C" "null")
                                                                                                             "empty"
                                                                                                             "empty"
                                                                                                             "empty"
                                                                                                             "empty"
                                                                                                             "empty"
                                                                                                             (make-cp "C" "null")))

(check-expect (charge-row (parse-grid Grid1) (parse-grid Grid1) (parse-row (third Grid1)) (make-posn 0 2)) (list
                                                                                                            (make-cp "C" "null")
                                                                                                            "empty"
                                                                                                            (make-gates "NOT" "RIGHT" "null")
                                                                                                            (make-cp "C" "null")
                                                                                                            (make-iowire "OUT" "OUTPUT" "null")
                                                                                                            "empty"
                                                                                                            (make-cp "C" "null")))

(check-expect (charge-row (parse-grid Grid1) (parse-grid Grid1) (parse-row (fourth Grid1)) (make-posn 0 3)) (list
                                                                                                             (make-cp "C" "null")
                                                                                                             "empty"
                                                                                                             (make-cp "C" "null")
                                                                                                             "empty"
                                                                                                             "empty"
                                                                                                             "empty"
                                                                                                             (make-cp "C" "null")))

(check-expect (charge-row (parse-grid Grid1) (parse-grid Grid1) (parse-row (fifth Grid1)) (make-posn 0 4)) (list
                                                                                                            (make-cp "C" "null")
                                                                                                            (make-cp "C" "null")
                                                                                                            (make-gates "AND" "UP" "null")
                                                                                                            (make-cp "C" "null")
                                                                                                            (make-cp "C" "null")
                                                                                                            (make-cp "C" "null")
                                                                                                            (make-cp "C" "null")))



(: get-charged-neighbors (AGrid APosn -> [ListOf ACell]))
; returns a list of all the charged neighbors
(define (get-charged-neighbors grid pos)
  (cond
    [(false? (get-neighbors grid pos)) (error "invalid posn" pos)]
    [else (filter (lambda (elem) (not (string? elem))) (get-neighbors grid pos))]))

(check-expect (get-charged-neighbors (parse-grid Grid1)
                                     (make-posn 2 0)) (list
                                                       (make-iowire "INP2" "INPUT" #false)
                                                       (make-iowire "INP1" "INPUT" #true)))

(check-expect (get-charged-neighbors (update-grid (parse-grid Grid1)) ; applies get-charged-neighbors on a grid where the first row has had some propogation of charge, as indicated by update-grid
                                     (make-posn 0 0)) (list
                                                       (make-iowire "INP1" "INPUT" #true)
                                                       (make-cp "C" "null")))

(check-expect (get-charged-neighbors (update-grid (parse-grid Grid1)) ; same concept as the previous check-expect
                                     (make-posn 4 0)) (list
                                                       (make-cp "C" "null")
                                                       (make-iowire "INP2" "INPUT" #false)))

(: get-charge ([ListOf ACell] ->  [ListOf ACharge]))
; returns a list of charges of all the charged neighbors
(define (get-charge lst)
  (cond
    [(empty? lst) '()]
    [(and (string? (first lst)) (string=? "empty" (first lst))) (get-charge (rest lst))]
    [(cp? (first lst)) (cons (cp-CHARGE (first lst)) (get-charge(rest lst))) ]
    [(gates? (first lst)) (cons (gates-CHARGE (first lst)) (get-charge(rest lst)))]
    [(iowire? (first lst)) (cons(iowire-CHARGE (first lst)) (get-charge(rest lst)))]))

(check-expect (get-charge (get-charged-neighbors (parse-grid Grid1)
                                                 (make-posn 2 0))) (list #false #true))

(check-expect (get-charge (get-charged-neighbors (update-grid (parse-grid Grid1)) ; applies get-charge on a grid where the first row has had some propogation of charge, as indicated by update-grid
                                                 (make-posn 0 0))) (list #true "null"))

(check-expect (get-charge (get-charged-neighbors (update-grid (parse-grid Grid1)) ; same concept as the previous check-expect
                                                 (make-posn 4 0))) (list "null" #false))




;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; CELL CHARGE FUNCTIONS ;;

;;;;;;;;;;;;;;;;;;;;;;;;;;;

   
(: cp-charge (AGrid AGrid ACell APosn -> ACell))
(define (cp-charge g g2 cell pos)
  ; updates each conductive plate's charge with the propogated charge
  (cond
    [(boolean? (cp-CHARGE cell)) cell]
    [(andmap (lambda (elem) (string? elem) ) (get-charge (get-charged-neighbors g2 pos)))
     (make-cp (cp-CONDUCTIVE-PLATE cell) "null")] 
    [(andmap (lambda (elem) (or (string? elem) (false? elem))) (get-charge (get-charged-neighbors g2 pos)))
     (make-cp (cp-CONDUCTIVE-PLATE cell) #f)] 
    [(andmap (lambda (elem)  (or (string? elem) (not (false? elem)))) (get-charge (get-charged-neighbors g2 pos)))
     (make-cp (cp-CONDUCTIVE-PLATE cell) #t)]
    [else (error "Conflicting charges")]))

(check-expect (cp-charge (parse-grid (list `(,cp-true ,S2 ,S7)))
                         (parse-grid (list `(,cp-true ,S2 ,S7)))
                         (parse-cell S2)
                         (make-posn 1 0)) (make-cp "C" #true))

(check-expect (cp-charge (parse-grid (list `(,cp-false ,S2 ,S7)))
                         (parse-grid (list `(,cp-false ,S2 ,S7)))
                         (parse-cell S2)
                         (make-posn 1 0)) (make-cp "C" #false))

(check-error (cp-charge (parse-grid (list `(,cp-true ,S2 ,cp-false ,S7)))
                        (parse-grid (list `(,cp-true ,S2 ,cp-false ,S7)))
                        (parse-cell S2)
                        (make-posn 1 0)))


(: output-charge (AGrid AGrid ACell APosn ->  ACell))
(define (output-charge g g2 cell pos)
  ; updates output wire's charge with the propogated charge
  (cond
    [(ormap (lambda (elem) (string? elem)) (get-charge (get-charged-neighbors g2 pos))) cell]
    [(andmap (lambda (elem) (or (string? elem) (false? elem))) (get-charge (get-charged-neighbors g2 pos)))
     (make-iowire (iowire-NAME-WIRE cell) OUTPUT-WIRE #f)] 
    [(andmap (lambda (elem)  (or (string? elem) (not (false? elem)))) (get-charge (get-charged-neighbors g2 pos)))
     (make-iowire (iowire-NAME-WIRE cell) OUTPUT-WIRE #t)]
    [else (error "Conflicting charges")]))

(check-expect (output-charge (parse-grid (list `(,cp-true ,cp-true ,S7)))
                             (parse-grid (list `(,cp-true ,cp-true ,S7)))
                             (parse-cell S7)
                             (make-posn 2 0)) (make-iowire "OUT" "OUTPUT" #true))

(check-expect (output-charge (parse-grid (list `(,cp-false ,cp-false ,S7)))
                             (parse-grid (list `(,cp-false ,cp-false ,S7)))
                             (parse-cell S7)
                             (make-posn 2 0)) (make-iowire "OUT" "OUTPUT" #false))


;examples to demonstrate the working of not-charge, and-charge and or-charge in check-expects
(define cp-true '("C" #true)) 
(define cp-false '("C" #false))

(: or-charge (AGrid AGate APosn -> AGate))
; updates the charge of an OR gate with respect to its surrounding inputs
(define (or-charge grid cell pos)
  (make-gates (gates-TYPE-GATE cell)
              (gates-DIRECTION cell)
              (ormap (lambda (x) x) (get-charge (filter-input grid (gates-DIRECTION cell) pos)))))

(check-expect (or-charge (parse-grid (list `(,S2 ,cp-true ,S12 ,cp-true ,S2 ,S2 ,S2)
                                           `(,S2 ,S1 ,S2 ,S1 ,S1 ,S1 ,S2)
                                           `(,S2 ,S1 ,S1 ,S1 ,S1 ,S1 ,S2)
                                           `(,S2 ,S1 ,S7 ,S1 ,S1 ,S1 ,S2)
                                           `(,S2 ,S2 ,S8 ,S2 ,S2 ,S2 ,S2)))
                         (parse-cell S12) (make-posn 2 0)) (make-gates "OR" "DOWN" #true))

(check-expect (or-charge (parse-grid (list `(,S2 ,cp-true ,S12 ,cp-false ,S2 ,S2 ,S2)
                                           `(,S2 ,S1 ,S2 ,S1 ,S1 ,S1 ,S2)
                                           `(,S2 ,S1 ,S1 ,S1 ,S1 ,S1 ,S2)
                                           `(,S2 ,S1 ,S7 ,S1 ,S1 ,S1 ,S2)
                                           `(,S2 ,S2 ,S8 ,S2 ,S2 ,S2 ,S2)))
                         (parse-cell S12) (make-posn 2 0)) (make-gates "OR" "DOWN" #true))

(check-expect (or-charge (parse-grid (list `(,S2 ,cp-false ,S12 ,cp-false ,S2 ,S2 ,S2)
                                           `(,S2 ,S1 ,S2 ,S1 ,S1 ,S1 ,S2)
                                           `(,S2 ,S1 ,S1 ,S1 ,S1 ,S1 ,S2)
                                           `(,S2 ,S1 ,S7 ,S1 ,S1 ,S1 ,S2)
                                           `(,S2 ,S2 ,S8 ,S2 ,S2 ,S2 ,S2)))
                         (parse-cell S12) (make-posn 2 0)) (make-gates "OR" "DOWN" #false))

(check-expect (or-charge (parse-grid (list `(,S2 ,cp-false ,S12 ,cp-true ,S2 ,S2 ,S2)
                                           `(,S2 ,S1 ,S2 ,S1 ,S1 ,S1 ,S2)
                                           `(,S2 ,S1 ,S1 ,S1 ,S1 ,S1 ,S2)
                                           `(,S2 ,S1 ,S7 ,S1 ,S1 ,S1 ,S2)
                                           `(,S2 ,S2 ,S8 ,S2 ,S2 ,S2 ,S2)))
                         (parse-cell S12) (make-posn 2 0)) (make-gates "OR" "DOWN" #true))

(: and-charge (AGrid AGate APosn -> AGate))
; updates the charge of an AND gate with respect to its surrounding inputs
(define (and-charge grid cell pos)
  (make-gates (gates-TYPE-GATE cell)
              (gates-DIRECTION cell)
              (andmap (lambda (x) x) (get-charge (filter-input grid (gates-DIRECTION cell) pos)))))

(check-expect (and-charge (parse-grid (list `(,S2 ,S2 ,S1 ,S19 ,S2 ,S7 ,S1)
                                            `(,S2 ,S1 ,S1 ,S2 ,S1 ,S1 ,S2)
                                            `(,S2 ,S2 ,cp-true ,S8 ,cp-true ,S2 ,S2)))
                          (parse-cell S8) (make-posn 3 2)) (make-gates "AND" "UP" #true))

(check-expect (and-charge (parse-grid (list `(,S2 ,S2 ,S1 ,S19 ,S2 ,S7 ,S1)
                                            `(,S2 ,S1 ,S1 ,S2 ,S1 ,S1 ,S2)
                                            `(,S2 ,S2 ,cp-true ,S8 ,cp-false ,S2 ,S2)))
                          (parse-cell S8) (make-posn 3 2)) (make-gates "AND" "UP" #false))

(check-expect (and-charge (parse-grid (list `(,S2 ,S2 ,S1 ,S19 ,S2 ,S7 ,S1)
                                            `(,S2 ,S1 ,S1 ,S2 ,S1 ,S1 ,S2)
                                            `(,S2 ,S2 ,cp-false ,S8 ,cp-false ,S2 ,S2)))
                          (parse-cell S8) (make-posn 3 2)) (make-gates "AND" "UP" #false))

(check-expect (and-charge (parse-grid (list `(,S2 ,S2 ,S1 ,S19 ,S2 ,S7 ,S1)
                                            `(,S2 ,S1 ,S1 ,S2 ,S1 ,S1 ,S2)
                                            `(,S2 ,S2 ,cp-false ,S8 ,cp-true ,S2 ,S2)))
                          (parse-cell S8) (make-posn 3 2)) (make-gates "AND" "UP" #false))
  

(: not-charge (AGrid AGate APosn -> AGate))
; updates the charge of an NOT gate with respect to its input
(define (not-charge grid cell pos)
  (if (= (length (get-charge (filter-input grid (gates-DIRECTION cell) pos))) 1) ; checks if there's only one input for the not gate
      (make-gates (gates-TYPE-GATE cell)
                  (gates-DIRECTION cell)
                  (not (first (get-charge (filter-input grid (gates-DIRECTION cell) pos)))))
      (error "wrong amount of NOT gate inputs")))


(check-error (not-charge (parse-grid (list `(,S2 ,S6 ,S2 ,S19 ,S2 ,S7 ,S1)
                                           `(,S2 ,S1 ,S1 ,S2 ,S1 ,S1 ,S2)
                                           `(,S2 ,S2 ,S2 ,S8 ,S2 ,S2 ,S2)))
                         (parse-cell S19) (make-posn 3 0)))

(check-expect (not-charge (parse-grid (list `(,S2 ,S2 ,S1 ,S19 ,S2 ,S7 ,S1)
                                            `(,S2 ,S1 ,S1 ,cp-true ,S1 ,S1 ,S2)
                                            `(,S2 ,S2 ,S2 ,S8 ,S2 ,S2 ,S2)))
                          (parse-cell S19) (make-posn 3 0)) (make-gates "NOT" "RIGHT" #false))
  
(: gates-charge (AGrid AGrid ACell APosn ->  ACell))
;updates each gate's charge with the propogated charge
(define (gates-charge g g2 cell pos)
  (cond
    [(boolean? (gates-CHARGE cell)) cell]
    
    [(string=? (gates-TYPE-GATE cell) OR) (if (any-neighbors-null? g2 cell pos)
                                              cell 
                                              (or-charge g2 cell pos))]
    
    [(string=? (gates-TYPE-GATE cell) AND) (if (any-neighbors-null? g2 cell pos)
                                               cell
                                               (and-charge g2 cell pos))]
    
    [(string=?  (gates-TYPE-GATE cell) NOT) (if (any-neighbors-null? g2 cell pos)
                                                cell
                                                (not-charge g2 cell pos))]))

(check-expect (gates-charge (parse-grid Grid1) (parse-grid Grid1) (parse-cell S12) (make-posn 2 0)) (make-gates "OR" "DOWN" #true))
(check-expect (gates-charge (parse-grid Grid1) (parse-grid Grid1) (parse-cell S18) (make-posn 2 4)) (make-gates "OR" "RIGHT" "null"))
(check-expect (gates-charge (parse-grid Grid1) (parse-grid Grid1) (parse-cell S19) (make-posn 2 2)) (make-gates "NOT" "RIGHT" "null"))



;;;;;;;;;;;;;;;;;;;;;;;

;;  INPUT AND OUTPUT ;;
;;   CELL FUNCTIONS  ;;

;;;;;;;;;;;;;;;;;;;;;;;

(: any-neighbors-null? (AGrid ACell APosn ->  ACharge))
; checks whether any input around a gate is uncharged
(define (any-neighbors-null? grid cell pos)
  (ormap (lambda (elem) (string? elem)) 
         (get-charge (remove-all '() (filter-input grid (gates-DIRECTION cell) pos)))))

(check-expect (any-neighbors-null? (parse-grid Grid1) (parse-cell S8) (make-posn 2 4)) #true)
(check-expect (any-neighbors-null? (parse-grid Grid1) (parse-cell S12) (make-posn 2 0)) #false)
(check-expect (any-neighbors-null? (parse-grid Grid1) (parse-cell S19) (make-posn 2 2)) #true)


(: filter-input (AGrid ADirection APosn -> [ListOf ACell]))
;filters the list of possible directions of cardinal neighbors of a cell and returns all the ones that aren't the output cell
(define (filter-input grid dir pos)
  (remove-all '() (map (lambda (x) (output-cell grid x pos)) ;removes any occurence of '() from the lists which were returned by the output-cell function
                       (filter (lambda (elem) (not (string=? elem dir))) (list UP DOWN LEFT RIGHT)))))

(check-expect (filter-input (parse-grid Grid1) DOWN (make-posn 2 0)) (list
                                                                      (parse-cell S6_1+)
                                                                      (parse-cell S6_2-)))

(check-expect (filter-input (parse-grid Grid1) UP (make-posn 2 4)) (list
                                                                    (parse-cell S2)
                                                                    (parse-cell S2)))

(check-expect (filter-input (parse-grid Grid1) RIGHT (make-posn 2 2)) (list
                                                                       (parse-cell S1)
                                                                       (parse-cell S2)
                                                                       (parse-cell S1)))



(: output-cell (AGrid ADirection APosn -> (mixed ACell EmptyList)))
; returns cells that are valid neighbors and '() if they are not
(define (output-cell grid dir pos)
  (cond
    [(string=? dir UP) (cond [(valid-posn? grid (make-posn (posn-x pos) (sub1(posn-y pos))))
                              (list-ref (list-ref grid (sub1(posn-y pos))) (posn-x pos))]
                             [else '()])]
    [(string=? dir RIGHT) (cond [(valid-posn? grid (make-posn (add1 (posn-x pos)) (posn-y pos)))
                                 (list-ref (list-ref grid (posn-y pos)) (add1 (posn-x pos)))]
                                [else '()])]
    [(string=? dir LEFT) (cond [(valid-posn? grid (make-posn (sub1 (posn-x pos)) (posn-y pos)))
                                (list-ref (list-ref grid (posn-y pos)) (sub1 (posn-x pos)))]
                               [else '()])]
    [(string=? dir DOWN)  (cond [(valid-posn? grid (make-posn (posn-x pos) (add1(posn-y pos))))
                                 (list-ref (list-ref grid (add1(posn-y pos))) (posn-x pos))]
                                [else '()])]))

(check-expect (output-cell (parse-grid Grid1) RIGHT (make-posn 6 0)) '())
(check-expect (output-cell (parse-grid Grid1) DOWN (make-posn 2 4)) '())
(check-expect (output-cell (parse-grid Grid1) UP (make-posn 3 0)) '())
(check-expect (output-cell (parse-grid Grid1) LEFT (make-posn 0 4)) '())
(check-expect (output-cell (parse-grid Grid1) RIGHT (make-posn 1 2)) (parse-cell S19))
(check-expect (output-cell (parse-grid Grid1) UP (make-posn 3 1)) (parse-cell S6_2-))
(check-expect (output-cell (parse-grid Grid1) RIGHT (make-posn 3 2)) (parse-cell S7))




;;;;;;;;;;;;;;;;;;;;;;

;;     BIG BANG     ;;

;;;;;;;;;;;;;;;;;;;;;;

; Exercise 5---------------------------------------------------------------------------------------------------------
(: main (AGrid -> AGrid))
(define (main g)
  ; big bang function
  (big-bang
      g
    (to-draw grid->image)
    (on-tick update-grid 0.75)))

;test
; (main (parse-grid Grid1))




;;;;;;;;;;;;;;;;;;;;;;

;;        9A        ;;

;;;;;;;;;;;;;;;;;;;;;;

; Exercise 2---------------------------------------------------------------------------------------------------------
; A World State is a (make-WorldState [AGrid [ListOf Goal] Boolean]
(define WS (signature (WorldstateOf AGrid SExp [ListOf [ListOf (mixed SExp Boolean)]] Boolean)))
(define-struct WorldState [grid goals-incomplete goals-complete set])
; - where a grid is the Grid defined earlier in the project
; - A Goal is defined below
; - goals-complete are the evaluated goals, either passed (true) or failed (false),
;   which is store as a boolean value with each goal.
;   We start with an empty list for complete goals as no goals have been completed and we need a base list to add our completed goals too once evaluated
; - goals-incomplete are the incompleted goals
;   Even if there is one goal, it must inputted as a (list goal) to work with our functions that use map/recursion to work over a list of goals.
; - A set is a boolean value which keeps track of whether
;   the grid has been set or not with the goal's inputs. That is why it's initial value is #false

(define Goal1 '( ((INP1 +) (INP2 -)) ((OUT +))))

(define Goal2 '(((INP1 -) (INP2 -)) ((OUT -))))

(define Goal3 '(((INP1 +) (INP2 -)) ((OUT1 +) (OUT2 -))))

;Format for parse-goal, parse-input and parse-output
; - A Goal is a [ListOf SExp] of the following format:
;  (list
;  (list (list INPUT-NAME1 INPUT-CHARGE1)
;        (list INPUT-NAME2 INPUT-CHARGE2)
;         etc.
;         (list INPUT-NAMEn INPUT-CHARGEn))
;  (list (list OUTPUT-NAME1 OUTPUT-CHARGE1)
;        (list OUTPUT-NAME2 OUTPUT-CHARGE2)
;        etc.
;        (list OUTPUT-NAMEm OUTPUT-CHARGEn)))
; - where INPUT-NAME and OUTPUT-NAME are symbols for the names of the iowires
; - and INPUT-CHARGE and OUTPUT-CHARGE is the symbol '+ or '-

(: parse-goal (SExp -> [ListOf AnIO-Wire]))
; parses a goal into a list of io-wires by combining the parsed inputs and outputs into one list
(define (parse-goal sexp)
  (append (map parse-input (first sexp)) (map parse-output (second sexp))))

(check-expect (parse-goal Goal1) (list (make-iowire "INP1" "INPUT" #true) (make-iowire "INP2" "INPUT" #false) (make-iowire "OUT" "OUTPUT" #true)))
(check-expect (parse-goal Goal2) (list
                                  (make-iowire "INP1" "INPUT" #false)
                                  (make-iowire "INP2" "INPUT" #false)
                                  (make-iowire "OUT" "OUTPUT" #false))) 


(: parse-input (SExp -> AnIO-Wire))
; parses an input wire

(define (parse-input sexpr)
  (cond [(number? sexpr) (error "Invalid Number")]
        [(string? sexpr)  (error "Invalid String")]
        [(boolean? sexpr) (error "Invalid Bool")]
        [(symbol? sexpr) (error "Invalid Sym")]
        [(list? sexpr) (cond [(and (= (length sexpr) 2)
                                   (symbol? (first sexpr))
                                   (symbol? (second sexpr)))
                                   (make-iowire (symbol->string (first sexpr)) "INPUT" (cond [(symbol=? (second sexpr) '+) #t]
                                                           [(symbol=? (second sexpr) '-) #f]))]
                             [else (error "Invalid Goal Input")])]))

(check-expect (parse-input (first (first Goal1))) (make-iowire "INP1" "INPUT" #true))
(check-expect (parse-input (second (first Goal1))) (make-iowire "INP2" "INPUT" #false))
(check-expect (parse-input (first (first Goal2))) (make-iowire "INP1" "INPUT" #false))
(check-expect (parse-input (second (first Goal2))) (make-iowire "INP2" "INPUT" #false))

(: parse-output (SExp -> AnIO-Wire))
; parses an output wire
(define (parse-output sexpr)
  (cond [(number? sexpr) (error "Invalid Number")]
        [(string? sexpr)  (error "Invalid String")]
        [(boolean? sexpr) (error "Invalid Bool")]
        [(symbol? sexpr) (error "Invalid Sym")]
        [(list? sexpr) (cond [(and (= (length sexpr) 2)
                                   (symbol? (first sexpr))
                                   (symbol? (second sexpr)))
                                   (make-iowire (symbol->string (first sexpr)) "OUTPUT" (cond [(symbol=? (second sexpr) '+) #t]
                                                           [(symbol=? (second sexpr) '-) #f]))]
                             [else (error "Invalid Goal Output")])]))

(check-expect (parse-output (first (second Goal1))) (make-iowire "OUT" "OUTPUT" #true))
(check-expect (parse-output (first (second Goal2))) (make-iowire "OUT" "OUTPUT" #false))
(check-expect (parse-output (first (second Goal3))) (make-iowire "OUT1" "OUTPUT" #true))
(check-expect (parse-output (second (second Goal3))) (make-iowire "OUT2" "OUTPUT" #false))

(: make-goals (SExp -> [ListOf [ListOf AnIO-Wire]]))
; parses a list of goals into a list of lists of resulting io-wires
(define (make-goals log)
  (map parse-goal log))

(check-expect (make-goals (list Goal1 Goal2)) (list
                                               (list
                                                (make-iowire "INP1" "INPUT" #true)
                                                (make-iowire "INP2" "INPUT" #false)
                                                (make-iowire "OUT" "OUTPUT" #true))
                                               (list
                                                (make-iowire "INP1" "INPUT" #false)
                                                (make-iowire "INP2" "INPUT" #false)
                                                (make-iowire "OUT" "OUTPUT" #false))))

; Exercise 3---------------------------------------------------------------------------------------------------------
(: draw (WS -> Image))
; new draw handler that displays the goals on the side of the grid as well as the state of the grid (running or won/lost)
(define (draw ws)
  (above/align "left"
               (game-won-lost ws)
               (beside (grid->image (WorldState-grid ws))
                       (square 5 "solid" "transparent")
                       (above/align "left"
                                    (text "INCOMPLETE GOAL(s):" 24 "black")
                                    (square 5 "solid" "transparent")
                                    (make-goal-text-incomplete (WorldState-goals-incomplete ws))
                                    (square 10 "solid" "transparent")
                                    (text "COMPLETE GOAL(s):" 24 "black")
                                    (square 5 "solid" "transparent")
                                    (make-goal-text-complete (WorldState-goals-complete ws))))))

(define (game-won-lost ws)
  (if (empty? (WorldState-goals-incomplete ws))
      (if (andmap second (WorldState-goals-complete ws))
          (text "WON" 24 "black")
          (text "LOST" 24 "black"))
      (text "RUNNING" 24 "black")))

(check-expect (game-won-lost (make-WorldState (parse-grid Grid1) '() (list (list (parse-goal Goal1) #t)) #t )) (text "WON" 24 "black"))
(check-expect (game-won-lost (make-WorldState (parse-grid Grid1) '() (list (list (parse-goal Goal2) #f)) #t )) (text "LOST" 24 "black"))

(: los->str ([ListOf String] -> String))
; combines the list of converted terms into a sentence
(define (los->str los)
  (cond [(empty? los) ""]
        [(cons? los) (string-append (first los) (los->str (rest los)))]))

(check-expect (los->str (list "abc" "def" "ghi")) "abcdefghi")
(check-expect (los->str (list "Hello, " "World!")) "Hello, World!")
(check-expect (los->str (list "")) "")

(: input->text (SExp -> String))
; converts the SExp for an input wire into text to be displayed in the game
(define (input->text sexp)
  (string-append " " (symbol->string (first sexp)) " is " (symbol->string (second sexp)) " and "))

(check-expect (input->text '(INP1 +)) " INP1 is + and ")
(check-expect (input->text '(INP2 -)) " INP2 is - and ")

(: output->text (SExp -> String))
; converts the SExp for an output wire into text to be displayed in the game
(define (output->text sexp)
  (string-append " " (symbol->string (first sexp)) " is " (symbol->string (second sexp)) " "))

(check-expect (output->text '(OUT1 -)) " OUT1 is - ")
(check-expect (output->text '(OUT2 +)) " OUT2 is + ")

(: goal->text (SExp -> Image))
; displays a goal in text form
(define (goal->text sexp)
  (text (string-append (los->str (map input->text (first sexp)))
                       (los->str (map output->text (second sexp)))) 24 "black"))

(check-expect (goal->text Goal1) (text " INP1 is + and  INP2 is - and  OUT is + " 24 "black"))
(check-expect (goal->text Goal2) (text " INP1 is - and  INP2 is - and  OUT is - " 24 "black"))

(: make-goal-text-incomplete (SExp -> Image))
; displays the incomplete goals in text form
(define (make-goal-text-incomplete los)
  (cond [(empty? los) (empty-scene 0 0)]
        [(cons? los) (above/align "left"
                                  (beside (square 5 "solid" "transparent")
                                          (square 24 "outline" "black")
                                          (goal->text (first los)))
                                  (square 10 "solid" "white")
                                  (make-goal-text-incomplete (rest los)))]))

(check-expect (make-goal-text-incomplete (list Goal1 Goal2))
              (above/align "left"
                           (beside (square 5 "solid" "transparent")
                                   (square 24 "outline" "black")
                                   (text " INP1 is + and  INP2 is - and  OUT is + " 24 "black"))
                           (square 10 "solid" "white")
                           (above/align "left"
                                        (beside (square 5 "solid" "transparent")
                                                (square 24 "outline" "black")
                                                (text " INP1 is - and  INP2 is - and  OUT is - " 24 "black"))
                                        (square 10 "solid" "white")
                                        (empty-scene 0 0))))

(: make-goal-text-complete ([ListOf [ListOf (mixed SExp Boolean)]] -> Image))
; displays the complete goals in text form
; a red box next to the goal indicates that the goal was not achieved
; a green box next to the goal indicates that the goal was achieved
(define (make-goal-text-complete los)
  (cond [(empty? los) (empty-scene 0 0)]
        [(cons? los) (above/align "left"
                                  (beside (square 5 "solid" "transparent")
                                          (if (second (first los))
                                              (square 24 "solid" "green")
                                              (square 24 "solid" "red"))
                                          (goal->text (first (first los))))
                                  (square 10 "solid" "white")
                                  (make-goal-text-complete (rest los)))]))

(check-expect (make-goal-text-complete (list (list Goal1 #t) (list Goal2 #f)))
              (above/align "left"
                           (beside (square 5 "solid" "transparent")
                                   (square 24 "solid" "green")
                                   (text " INP1 is + and  INP2 is - and  OUT is + " 24 "black"))
                           (square 10 "solid" "white")
                           (above/align "left"
                                        (beside (square 5 "solid" "transparent")
                                                (square 24 "solid" "red")
                                                (text " INP1 is - and  INP2 is - and  OUT is - " 24 "black"))
                                        (square 10 "solid" "white")
                                        (empty-scene 0 0))))
  

; Exercise 4---------------------------------------------------------------------------------------------------------
(: run-goals (WS -> WS))
; runs through each goal over the grid
(define (run-goals ws)
  (cond [(empty? (make-goals (WorldState-goals-incomplete ws))) ws] 
        [(cons? (make-goals (WorldState-goals-incomplete ws))) (if (not (WorldState-set ws))
                                                                   (make-WorldState (set-grid (WorldState-grid ws) (first (make-goals (WorldState-goals-incomplete ws))))
                                                                                    (WorldState-goals-incomplete ws)
                                                                                    (WorldState-goals-complete ws)
                                                                                    #t)
                                                                   (if (equal? (WorldState-grid ws) (update-grid (WorldState-grid ws))) ; not using goal-met?
                                                                       (make-WorldState (WorldState-grid ws)
                                                                                        (rest (WorldState-goals-incomplete ws))
                                                                                        (append (WorldState-goals-complete ws)
                                                                                                (list (if (goal-met? (WorldState-grid ws) (parse-goal (first (WorldState-goals-incomplete ws))))
                                                                                                          (list (first (WorldState-goals-incomplete ws)) #t) ; we add this boolean to determine if the goal has passed or failed, not just that it has been evaluated
                                                                                                          (list (first (WorldState-goals-incomplete ws)) #f))))
                                                                                        #f)
                                                                       (make-WorldState (update-grid (WorldState-grid ws))
                                                                                        (WorldState-goals-incomplete ws)
                                                                                        (WorldState-goals-complete ws)
                                                                                        #t)))])) 
                                            
(: goal-met? (AGrid [ListOf AnIO-Wire] -> Boolean))
; returns if a particular goal have been met in the grid
(define (goal-met? g low)
  (cond
    [(empty? low) #t]
    [(string=? (iowire-TYPE-WIRE (first low)) OUTPUT-WIRE) (if (search-grid-output g (first low))
                                                               (goal-met? g (rest low))
                                                               #f)]
    [else (goal-met? g (rest low))]))


(: set-grid (AGrid [ListOf AnIO-Wire] -> AGrid))
;sets a grid according to the inputs of the goal
(define (set-grid g low)
  (cond
    [(empty? low) g]
    [(string=? (iowire-TYPE-WIRE (first low)) INPUT-WIRE) (set-grid (search-grid-input g (first low)) (rest low))]
    [else (set-grid g (rest low))]))

(check-expect (set-grid (parse-grid Grid1) (parse-goal Goal2)) (list (list (make-cp "C" "null")
                                                                           (make-iowire "INP1" "INPUT" #false) ; note the changes
                                                                           (make-gates "OR" "DOWN" "null")
                                                                           (make-iowire "INP2" "INPUT" #false) ; note the changes
                                                                           (make-cp "C" "null")
                                                                           (make-cp "C" "null")
                                                                           (make-cp "C" "null"))
                                                                     (list (make-cp "C" "null") "empty" "empty" "empty" "empty" "empty" (make-cp "C" "null"))
                                                                     (list (make-cp "C" "null") "empty" (make-gates "NOT" "RIGHT" "null") (make-cp "C" "null") (make-iowire "OUT" "OUTPUT" "null") "empty" (make-cp "C" "null"))
                                                                     (list (make-cp "C" "null") "empty" (make-cp "C" "null") "empty" "empty" "empty" (make-cp "C" "null"))
                                                                     (list (make-cp "C" "null") (make-cp "C" "null") (make-gates "AND" "UP" "null") (make-cp "C" "null") (make-cp "C" "null") (make-cp "C" "null") (make-cp "C" "null"))))

(: search-grid-input (AGrid AnIO-Wire -> AGrid))
; searches the grid for the input wire
(define (search-grid-input g input)
  (map (lambda (row) (search-row-input row input)) g))

(check-expect (search-grid-input (parse-grid Grid1) (make-iowire "INP2" "INPUT" #true)) (list (list (make-cp "C" "null")
                                                                                                    (make-iowire "INP1" "INPUT" #true)
                                                                                                    (make-gates "OR" "DOWN" "null")
                                                                                                    (make-iowire "INP2" "INPUT" #true) ; note the change
                                                                                                    (make-cp "C" "null")
                                                                                                    (make-cp "C" "null")
                                                                                                    (make-cp "C" "null"))
                                                                                              (list (make-cp "C" "null") "empty" "empty" "empty" "empty" "empty" (make-cp "C" "null"))
                                                                                              (list (make-cp "C" "null") "empty" (make-gates "NOT" "RIGHT" "null") (make-cp "C" "null") (make-iowire "OUT" "OUTPUT" "null") "empty" (make-cp "C" "null"))
                                                                                              (list (make-cp "C" "null") "empty" (make-cp "C" "null") "empty" "empty" "empty" (make-cp "C" "null"))
                                                                                              (list (make-cp "C" "null") (make-cp "C" "null") (make-gates "AND" "UP" "null") (make-cp "C" "null") (make-cp "C" "null") (make-cp "C" "null") (make-cp "C" "null"))))

(check-expect (search-grid-input (parse-grid Grid1) (make-iowire "INP2" "INPUT" #false)) (list (list (make-cp "C" "null")
                                                                                                     (make-iowire "INP1" "INPUT" #true)
                                                                                                     (make-gates "OR" "DOWN" "null")
                                                                                                     (make-iowire "INP2" "INPUT" #false) ; note the non-change
                                                                                                     (make-cp "C" "null")
                                                                                                     (make-cp "C" "null")
                                                                                                     (make-cp "C" "null"))
                                                                                               (list (make-cp "C" "null") "empty" "empty" "empty" "empty" "empty" (make-cp "C" "null"))
                                                                                               (list (make-cp "C" "null") "empty" (make-gates "NOT" "RIGHT" "null") (make-cp "C" "null") (make-iowire "OUT" "OUTPUT" "null") "empty" (make-cp "C" "null"))
                                                                                               (list (make-cp "C" "null") "empty" (make-cp "C" "null") "empty" "empty" "empty" (make-cp "C" "null"))
                                                                                               (list (make-cp "C" "null") (make-cp "C" "null") (make-gates "AND" "UP" "null") (make-cp "C" "null") (make-cp "C" "null") (make-cp "C" "null") (make-cp "C" "null"))))


(: search-row-input ([ListOf ACell] AnIO-Wire -> [ListOf ACell]))
; searches each row of a grid sfor the input wire
(define (search-row-input row input)
  (map (lambda (cell) (if
                       (and (iowire? cell)
                            (string=? (iowire-NAME-WIRE cell) (iowire-NAME-WIRE input))) input (set-to-null cell))) row))

(check-expect (search-row-input (first (parse-grid Grid1)) (make-iowire "INP2" "INPUT" #true)) (list (make-cp "C" "null")
                                                                                                    (make-iowire "INP1" "INPUT" #true)
                                                                                                    (make-gates "OR" "DOWN" "null")
                                                                                                    (make-iowire "INP2" "INPUT" #true) ; note the change
                                                                                                    (make-cp "C" "null")
                                                                                                    (make-cp "C" "null")
                                                                                                    (make-cp "C" "null")))

(check-expect (search-row-input (first (parse-grid Grid1)) (make-iowire "INP2" "INPUT" #false)) (list (make-cp "C" "null")
                                                                                                    (make-iowire "INP1" "INPUT" #true)
                                                                                                    (make-gates "OR" "DOWN" "null")
                                                                                                    (make-iowire "INP2" "INPUT" #false) ; note the non-change
                                                                                                    (make-cp "C" "null")
                                                                                                    (make-cp "C" "null")
                                                                                                    (make-cp "C" "null")))

(check-expect (search-row-input (second (parse-grid Grid1)) (make-iowire "INP2" "INPUT" #false)) (second (parse-grid Grid1))) ; nothing changes

(: set-to-null (ACell -> ACell))
; sets all cells that aren't iowires to null
(define (set-to-null cell)
  (cond [(cp? cell) (make-cp (cp-CONDUCTIVE-PLATE cell) "null")]
        [(and (string? cell) (string=? cell "empty")) "empty" ]
        [(and (iowire? cell) (string=? (iowire-TYPE-WIRE cell) "OUTPUT"))(make-iowire (iowire-NAME-WIRE cell) (iowire-TYPE-WIRE cell) "null")] 
        [(and (iowire? cell) (string=? (iowire-TYPE-WIRE cell) "INPUT")) cell] ; we need to return something for iowires
        [(gates? cell) (make-gates (gates-TYPE-GATE cell) (gates-DIRECTION cell) "null")]))

(check-expect (set-to-null "empty") "empty")
(check-expect (set-to-null (make-cp "C" #t)) (make-cp "C" "null"))
(check-expect (set-to-null (make-cp "C" #f)) (make-cp "C" "null"))
(check-expect (set-to-null (make-cp "C" "null")) (make-cp "C" "null"))
(check-expect (set-to-null (make-iowire "INP1" "INPUT" "null")) (make-iowire "INP1" "INPUT" "null"))
(check-expect (set-to-null (make-iowire "INP1" "INPUT" #t)) (make-iowire "INP1" "INPUT" #t))
(check-expect (set-to-null (make-iowire "INP1" "INPUT" #f)) (make-iowire "INP1" "INPUT" #f))
(check-expect (set-to-null (make-iowire "INP1" "OUTPUT" "null")) (make-iowire "INP1" "OUTPUT" "null"))
(check-expect (set-to-null (make-iowire "INP1" "OUTPUT" #t)) (make-iowire "INP1" "OUTPUT" "null"))
(check-expect (set-to-null (make-iowire "INP1" "OUTPUT" #f)) (make-iowire "INP1" "OUTPUT" "null"))
(check-expect (set-to-null (make-gates "AND" "RIGHT" #f)) (make-gates "AND" "RIGHT" "null"))
(check-expect (set-to-null (make-gates "AND" "LEFT" #f)) (make-gates "AND" "LEFT" "null"))
(check-expect (set-to-null (make-gates "AND" "UP" #f)) (make-gates "AND" "UP" "null"))
(check-expect (set-to-null (make-gates "AND" "DOWN" #f)) (make-gates "AND" "DOWN" "null"))
(check-expect (set-to-null (make-gates "AND" "RIGHT" #t)) (make-gates "AND" "RIGHT" "null"))
(check-expect (set-to-null (make-gates "AND" "LEFT" #t)) (make-gates "AND" "LEFT" "null"))
(check-expect (set-to-null (make-gates "AND" "UP" #t)) (make-gates "AND" "UP" "null"))
(check-expect (set-to-null (make-gates "AND" "DOWN" #t)) (make-gates "AND" "DOWN" "null"))
(check-expect (set-to-null (make-gates "AND" "RIGHT" "null")) (make-gates "AND" "RIGHT" "null"))
(check-expect (set-to-null (make-gates "AND" "LEFT" "null")) (make-gates "AND" "LEFT" "null"))
(check-expect (set-to-null (make-gates "AND" "UP" "null")) (make-gates "AND" "UP" "null"))
(check-expect (set-to-null (make-gates "AND" "DOWN" "null")) (make-gates "AND" "DOWN" "null"))
(check-expect (set-to-null (make-gates "OR" "RIGHT" #f)) (make-gates "OR" "RIGHT" "null"))
(check-expect (set-to-null (make-gates "OR" "LEFT" #f)) (make-gates "OR" "LEFT" "null"))
(check-expect (set-to-null (make-gates "OR" "UP" #f)) (make-gates "OR" "UP" "null"))
(check-expect (set-to-null (make-gates "OR" "DOWN" #f)) (make-gates "OR" "DOWN" "null"))
(check-expect (set-to-null (make-gates "OR" "RIGHT" #t)) (make-gates "OR" "RIGHT" "null"))
(check-expect (set-to-null (make-gates "OR" "LEFT" #t)) (make-gates "OR" "LEFT" "null"))
(check-expect (set-to-null (make-gates "OR" "UP" #t)) (make-gates "OR" "UP" "null"))
(check-expect (set-to-null (make-gates "OR" "DOWN" #t)) (make-gates "OR" "DOWN" "null"))
(check-expect (set-to-null (make-gates "OR" "RIGHT" "null")) (make-gates "OR" "RIGHT" "null"))
(check-expect (set-to-null (make-gates "OR" "LEFT" "null")) (make-gates "OR" "LEFT" "null"))
(check-expect (set-to-null (make-gates "OR" "UP" "null")) (make-gates "OR" "UP" "null"))
(check-expect (set-to-null (make-gates "OR" "DOWN" "null")) (make-gates "OR" "DOWN" "null"))
(check-expect (set-to-null (make-gates "NOT" "RIGHT" #f)) (make-gates "NOT" "RIGHT" "null"))
(check-expect (set-to-null (make-gates "NOT" "LEFT" #f)) (make-gates "NOT" "LEFT" "null"))
(check-expect (set-to-null (make-gates "NOT" "UP" #f)) (make-gates "NOT" "UP" "null"))
(check-expect (set-to-null (make-gates "NOT" "DOWN" #f)) (make-gates "NOT" "DOWN" "null"))
(check-expect (set-to-null (make-gates "NOT" "RIGHT" #t)) (make-gates "NOT" "RIGHT" "null"))
(check-expect (set-to-null (make-gates "NOT" "LEFT" #t)) (make-gates "NOT" "LEFT" "null"))
(check-expect (set-to-null (make-gates "NOT" "UP" #t)) (make-gates "NOT" "UP" "null"))
(check-expect (set-to-null (make-gates "NOT" "DOWN" #t)) (make-gates "NOT" "DOWN" "null"))
(check-expect (set-to-null (make-gates "NOT" "RIGHT" "null")) (make-gates "NOT" "RIGHT" "null"))
(check-expect (set-to-null (make-gates "NOT" "LEFT" "null")) (make-gates "NOT" "LEFT" "null"))
(check-expect (set-to-null (make-gates "NOT" "UP" "null")) (make-gates "NOT" "UP" "null"))
(check-expect (set-to-null (make-gates "NOT" "DOWN" "null")) (make-gates "NOT" "DOWN" "null"))

(: search-grid-output (AGrid AnIO-Wire -> Boolean))
; searches a grid for the output cell
(define (search-grid-output g output)
  (ormap (lambda (x) x) (map (lambda (row) (search-row-output row output)) g)))

(: search-row-output ([ListOf ACell] AnIO-Wire -> Boolean))
; searches each row of a grid for the output cell
(define (search-row-output row output)
  (cond
    [(empty? row) #f]
    [(and (iowire? (first row))
          (string=? (iowire-NAME-WIRE (first row)) (iowire-NAME-WIRE output))) (if (boolean=? (iowire-CHARGE (first row)) (iowire-CHARGE output))
                                                                                   #t #f)]
    [else (search-row-output (rest row) output)]))

; Exercise 5---------------------------------------------------------------------------------------------------------
; the input would be in the form (mainv2 (make-WorldState (parse-grid your_solution) your_puzzle '() #false))
(: mainv2 (WS -> WS))
(define (mainv2 world)
  ; big bang function
  (big-bang
      world
    (to-draw draw)
    (on-tick run-goals 0.75)
    (display-mode 'fullscreen)))


; Note:
; In our tests for main, we don't call make-goals in the call since it needs to recursively go
; through the original list of S-Expressions in our run-goals handler, and because it calls
; make-goals in the handler itself.

; some tests
;(mainv2 (make-WorldState (parse-grid Solution1) (puzzle-goals Puzzle1) '() #f))
;(mainv2 (make-WorldState (parse-grid Solution2) (puzzle-goals Puzzle2) '() #f))
                                                                                               
; Exercise 6---------------------------------------------------------------------------------------------------------

; Some new rows for the solution grids

; Solution 1
(define R6 `(,S2 ,S2 ,S2 ,S2 ,S2 ,S7_2 ,S1 ,S1))
(define R7 `(,S6_2- ,S1 ,S1 ,S1 ,S1 ,S1 ,S1 ,S1))
(define R8 `(,S2 ,S2 ,S2 ,S2 ,S2 ,S1 ,S7_1 ,S1))
(define R9 `(,S1 ,S1 ,S1 ,S1 ,S2 ,S1 ,S2 ,S1))
(define R10 `(,S6_1+ ,S2 ,S2 ,S2 ,S18 ,S2 ,S2 ,S1))

; Solution 2
(define R11 `(,S7_2 ,S14 ,S2 ,S2 ,S2 ,S1 ,S1 ,S1))
(define R12 `(,S1 ,S2 ,S1 ,S1 ,S2 ,S1 ,S1 ,S1))
(define R13 `(,S1 ,S2 ,S1 ,S1 ,S10 ,S2 ,S2 ,S1))
(define R14 `(,S1 ,S2 ,S2 ,S1 ,S1 ,S1 ,S6_2+ ,S2))
(define R15 `(,S1 ,S1 ,S2 ,S2 ,S1 ,S1 ,S1 ,S2))
(define R16 `(,S1 ,S1 ,S1 ,S6_1+ ,S1 ,S1 ,S2 ,S2))
(define R17 `(,S1 ,S1 ,S1 ,S2 ,S1 ,S1 ,S2 ,S1))
(define R18 `(,S1 ,S1 ,S1 ,S2 ,S2 ,S19 ,S18 ,S7_1))


; A Puzzle is a (make-struct [AGrid [ListOf Goal])
(define-struct puzzle [empty-grid goals])
; Interpretation:
; An empty grid of inputs and outputs which a player has to fill in
; according to the goals set and try to make the game work and pass all goals.


; As defined earlier in the project:
; - S1 represents an empty cell
; - S6_null, S6_1_null, S6_2_null represent input wires with no charge
; - S7, S7_1, S7_2 represent output wires with no charge

(define Puzzle1 (make-puzzle (list `(,S1 ,S1 ,S1 ,S1 ,S1 ,S7_2 ,S1 ,S1)
                                   `(,S6_2_null ,S1 ,S1 ,S1 ,S1 ,S1 ,S1 ,S1)
                                   `(,S1 ,S1 ,S1 ,S1 ,S1 ,S1 ,S7_1 ,S1)
                                   `(,S1 ,S1 ,S1 ,S1 ,S1 ,S1 ,S1 ,S1)
                                   `(,S6_1_null ,S1 ,S1 ,S1 ,S1 ,S1 ,S1 ,S1))
                             
                             (list '(((INP1 +) (INP2 +)) ((OUT1 +) (OUT2 +)))
                                   '(((INP1 +) (INP2 -)) ((OUT1 +) (OUT2 -)))
                                   '(((INP1 -) (INP2 +)) ((OUT1 +) (OUT2 +)))
                                   '(((INP1 -) (INP2 -)) ((OUT1 -) (OUT2 -))))))

(define Puzzle2 (make-puzzle (list `(,S7_2 ,S1 ,S1 ,S1 ,S1 ,S1 ,S1 ,S1)
                                   `(,S1 ,S1 ,S1 ,S1 ,S1 ,S1 ,S1 ,S1)
                                   `(,S1 ,S1 ,S1 ,S1 ,S1 ,S1 ,S1 ,S1)
                                   `(,S1 ,S1 ,S1 ,S1 ,S1 ,S1 ,S6_2_null ,S1)
                                   `(,S1 ,S1 ,S1 ,S1 ,S1 ,S1 ,S1 ,S1)
                                   `(,S1 ,S1 ,S1 ,S6_1_null ,S1 ,S1 ,S1 ,S1)
                                   `(,S1 ,S1 ,S1 ,S1 ,S1 ,S1 ,S1 ,S1)
                                   `(,S1 ,S1 ,S1 ,S1 ,S1 ,S1 ,S1 ,S7_1))
                             
                             (list '(((INP1 +) (INP2 +)) ((OUT1 +) (OUT2 -)))
                                   '(((INP1 +) (INP2 -)) ((OUT1 -) (OUT2 +))) 
                                   '(((INP1 -) (INP2 +)) ((OUT1 +) (OUT2 -)))
                                   '(((INP1 -) (INP2 -)) ((OUT1 +) (OUT2 -))))))

; Visual representations of the puzzles:
(define print-puzzle1 (draw (make-WorldState (parse-grid (puzzle-empty-grid Puzzle1)) (puzzle-goals Puzzle1) '() #f)))
(define print-puzzle2 (draw (make-WorldState (parse-grid (puzzle-empty-grid Puzzle2)) (puzzle-goals Puzzle2) '() #f)))


; A Solution is essentially a grid that can simulate all goals in a puzzle successfully.

(define Solution (list R6 R7 R8 R9 R10))
(define Solution2 (list R11 R12 R13 R14 R15 R16 R17 R18))






