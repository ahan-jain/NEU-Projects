#lang htdp/isl

(require 2htdp/image)
(require 2htdp/universe)

(define Image (signature (predicate image?)))
(define KeyEvent (signature (predicate key-event?)))


;EXERCISE 1 & 2 --------------------------------------------------------------------------------------

; letters is a (make-letters [[ListOf String] String])
(define-struct letters (LOL dist-letter))

; Where:

; An LOL (ListOfLetters) is a (cons String [ListOf String])
; Interpretation: Represents a list of unique letters where:
; - '() is the empty list
; - (cons l [ListOf String]) is the LOL with l prepended to the front
; dist-letter is the distinguished letter in the spelling bee

(define SomeLetters (signature (LettersOf LOL String)))
(define LOL (signature (ListOf String)))

;templates for LOL and letters

(: lol-temp (LOL -> Any))
(define (lol-temp l)
  (cond [(empty? l) ...]
        [(cons? l) (... (first l) ... (lol-temp (rest l)) ... )]))

(: letters-temp (SomeLetters -> Any))
(define (letters-temp l)
  (... (lol-temp (letters-LOL l)) ...
       (letters-dist-letter l) ... ))


; Examples for LOL:
(define LOL1 '())
(define LOL2 (cons "a" LOL1))
(define LOL3 (cons "c" LOL2))
(define LOL4 (cons "f" LOL3))
(define LOL5 (cons "d" LOL4))
(define LOL6 (cons "j" LOL5))
(define LOL7 (cons "y" LOL6))

; Examples for letters:
(define L1 (make-letters LOL1 "g"))
(define L2 (make-letters LOL3 "f"))
(define L3 (make-letters LOL5 "m"))
(define L4 (make-letters LOL4 "e"))
(define L5 (make-letters LOL6 "r"))
(define L6 (make-letters LOL7 "s"))

(: display-lol (SomeLetters -> Image))
; converts the LOL into an image and displays the letters side by side in text form
(define (display-lol l)
  (beside (text (first (letters-LOL l)) 24 "black")
          (letters->image (make-letters (rest (letters-LOL l)) (letters-dist-letter l)))))

(: letters->image (SomeLetters -> Image))
; display the letters for a spelling bee where the letters are side by side and black, except
; making the distinguished letter as red
(define (letters->image l)
  (cond [(empty? (letters-LOL l)) (text (letters-dist-letter l) 24 "red")]
        [(cons? (letters-LOL l)) (display-lol l)]))

(check-expect (letters->image L4) (beside (text "f" 24 "black")
                                          (text "c" 24 "black")
                                          (text "a" 24 "black")
                                          (text "e" 24 "red")))

(check-expect (letters->image L6) (beside (text "y" 24 "black")
                                          (text "j" 24 "black")
                                          (text "d" 24 "black")
                                          (text "f" 24 "black")
                                          (text "c" 24 "black")
                                          (text "a" 24 "black")
                                          (text "s" 24 "red")))

(check-expect (letters->image L5) (beside (text "j" 24 "black")
                                          (text "d" 24 "black")
                                          (text "f" 24 "black")
                                          (text "c" 24 "black")
                                          (text "a" 24 "black")
                                          (text "r" 24 "red")))

;EXERCISE 3 - 5 --------------------------------------------------------------------------------------

; A World is a (make-world Letters String Boolean)
(define-struct world (available-letters partial-word contains-dl))
(define AWorld (signature (WorldOf Letters String Boolean)))
; Where:

; - available-letters represents the letters that can be used by the player in the spelling bee
; - partial-word represents the word the player has formed so far
; - contains-dl represents a state of true or false for if the partial word contains the
;   distinguished letter

; Examples
(define W0 (make-world L1 "g" #t))
(define W1 (make-world L3 "dad" #f))
(define W2 (make-world L5 "car" #t))
(define W3 (make-world L6 "" #f))

; template
(: world-temp (AWorld -> Any))

(define (world-temp w)
  (... (letters-temp (world-available-letters w)) ...
       (world-partial-word w) ... (world-contains-dl w) ...))


(: world->image (AWorld -> Image))
; Takes in a World and produces an image, in the form of the available letters in black
; placed side by side on the top of the screen, followed by the distinguished letter in red
; and displays the partial word beneath it
(define (world->image w)
  (above (letters->image (world-available-letters w))      
                  (text (world-partial-word w) 18 "black" )))


(check-expect (world->image W0) (above (letters->image (world-available-letters W0))      
                                       (text "g" 18 "black" )))
(check-expect (world->image W1) (above (letters->image (world-available-letters W1))      
                                       (text "dad" 18 "black" )))
(check-expect (world->image W2) (above (letters->image (world-available-letters W2))      
                                       (text "car" 18 "black" )))
(check-expect (world->image W3) (above (letters->image (world-available-letters W3))      
                                       (text "" 18 "black" )))



(: key-pressed (World KeyEvent -> World))
; produces a new world when an available letter is pressed and adds that letter to the end of the
; partial word, and produces a new world with the partial word reset when the enter key is pressed,
; as long as the word entered has the distinguished letter
(define (key-pressed w a-key) 
  (cond [(member? a-key (letters-LOL (world-available-letters w)))
         (make-world (world-available-letters w)
                     (string-append (world-partial-word w) a-key)
                     (world-contains-dl w))]
        [(key=? a-key (letters-dist-letter (world-available-letters w)))
         (make-world (world-available-letters w)
                     (string-append (world-partial-word w) a-key)
                     #true)]
        [(and (key=? a-key "\r") (world-contains-dl w))
         (make-world (world-available-letters w)
                     ""
                     #f)]
        [else w]))


(check-expect (key-pressed W2 "\r") (make-world (world-available-letters W2)
                                                ""
                                                #f))

(check-expect (key-pressed W1 "m") (make-world (world-available-letters W1)
                                              "dadm"
                                              #t))

(check-expect (key-pressed W1 "d") (make-world (world-available-letters W1)
                                              "dadd"
                                              #f))

(check-expect (key-pressed W3 "y") (make-world (world-available-letters W3)
                                              "y"
                                              #f))

(check-expect (key-pressed W3 "s") (make-world (world-available-letters W3)
                                              "s"
                                              #t))

(check-expect (key-pressed W1 "\r") (make-world (world-available-letters W1)
                                              "dad"
                                              #f))

;EXERCISE 6 ------------------------------------------------------------------------------------------

(: play (World -> World))
; Uses big-bang to play a game of Spelling Bee, given Letters.
(define (play w)
  (big-bang
      w
     (to-draw world->image)
     (on-key key-pressed)
     (display-mode 'fullscreen)))

;EXERCISE 7 ------------------------------------------------------------------------------------------

; A Worldv2 is a (make-worldv2 Letters String Boolean [ListOf String])
(define-struct worldv2 (available-letters partial-word contains-dl LOW))
(define AWorldv2 (signature (Worldv2Of Letters String Boolean [ListOf String])))
; Where:

; - available-letters represents the letters that can be used by the player in the spelling bee
; - partial-word represents the word the player has formed so far
; - contains-dl represents a state of true or false for if the partial word contains the
;   distinguished letter
; - LOW represents a list of the words that the user has successfully found

; Examples
(define Wv2_0 (make-worldv2 L1 "" #f '()))
Instructor
| 09/29 at 8:54 pm
Grading comment:
You should include an example where contains-dl is #t.


(define Wv2_1 (make-worldv2 L3 "dad" #f (cons "facdm" '())))

(define Wv2_2 (make-worldv2 L5 "car" #t (cons "jard"
                                              (cons "arcf" '()))))

(define Wv2_3 (make-worldv2 L6 "" #f (cons "jayjays"
                                           (cons "cafds"
                                                 (cons "drasm"
                                                       (cons "cafds"
                                                             (cons "s" '())))))))

; template
(: worldv2-temp (AWorldv2 -> Any))

(define (worldv2-temp w2)
  (... (letters-temp (worldv2-available-letters w2)) ...
       (worldv2-partial-word w2) ... (worldv2-contains-dl w2) ... (worldv2-LOW w2) ...))
Instructor
| 09/29 at 8:56 pm
Grading comment:
You should call templates for worldv2-contains-dl and worldv2-LOW.


(: key-pressedv2 (AWorldv2 KeyEvent -> AWorldv2))

; produces a new world when an available letter is pressed and adds that letter to the end of the
; partial word, and produces a new world when the enter key is pressed with that partial word
; appended to a LOW, as long as the word entered has the distinguished letter

(define (key-pressedv2 w2 a-key) 
  (cond [(member? a-key (letters-LOL (worldv2-available-letters w2)))
         (make-worldv2 (worldv2-available-letters w2)
                       (string-append (worldv2-partial-word w2) a-key)
                       (worldv2-contains-dl w2)
                       (worldv2-LOW w2))]
        [(key=? a-key (letters-dist-letter (worldv2-available-letters w2)))
         (make-worldv2 (worldv2-available-letters w2)
                       (string-append (worldv2-partial-word w2) a-key)
                       #true
                       (worldv2-LOW w2))]
        [(and (key=? a-key "\r") (worldv2-contains-dl w2))
         (make-worldv2 (worldv2-available-letters w2)
                       ""
                       #f
                       (cons (worldv2-partial-word w2) (worldv2-LOW w2)))]
        [else w2]))



(: LOW->image ([ListOf String] -> Image))
; Converts a ListofWords to an Image, in the form of a list of words placed one on top of the other
(define (LOW->image l)
  (cond [(empty? l) (text " " 1 "black")]
        [(cons? l) (above (text (first l) 18 "black") (LOW->image (rest l)))]))

  

(: world->imagev2 (AWorldv2 -> Image))
; Takes in a Worldv2 and produces an image, in the form of the available letters in black
; placed side by side on the top of the screen, followed by the distinguished letter in red
; and displays the partial word beneath it, below which is the list of words previously entered
(define (world->imagev2 w2)
  (above (letters->image (worldv2-available-letters w2))
         (text (worldv2-partial-word w2) 18 "black" )
         (LOW->image (worldv2-LOW w2))))

(check-expect (world->imagev2 Wv2_0) (above (letters->image (worldv2-available-letters Wv2_0))      
                                       (text "" 18 "black" )
                                       (LOW->image (worldv2-LOW Wv2_0))))
(check-expect (world->imagev2 Wv2_1) (above (letters->image (worldv2-available-letters Wv2_1))      
                                       (text "dad" 18 "black" )
                                       (LOW->image (worldv2-LOW Wv2_1))))
(check-expect (world->imagev2 Wv2_2) (above (letters->image (worldv2-available-letters Wv2_2))      
                                       (text "car" 18 "black" )
                                       (LOW->image (worldv2-LOW Wv2_2))))
(check-expect (world->imagev2 Wv2_3) (above (letters->image (worldv2-available-letters Wv2_3))      
                                       (text "" 18 "black" )
                                       (LOW->image (worldv2-LOW Wv2_3))))




(: playv2 (Worldv2 -> Worldv2))
; Uses big-bang to play a game of Spelling Bee V2, given Letters.
(define (playv2 w2)
  (big-bang
      w2
    (to-draw world->imagev2)
    (on-key key-pressedv2)
    (display-mode 'fullscreen)))

;Tests to try out
;(playv2 Wv2_2)
;(playv2 Wv2_1)
;(play W2)
;(play W1)
