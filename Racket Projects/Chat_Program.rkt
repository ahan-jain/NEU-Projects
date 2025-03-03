#lang htdp/isl+
(require 2htdp/image)
(require 2htdp/universe)

(define Package (signature (predicate package?)))
(define Image (signature (predicate image?)))

(define UserMessage (signature [ListOf (mixed Symbol [ListOf String])]))
#| UserMessage: (list 'users [ListOf String]): A list of all connected client names.
   These are the same names as will be attached to messages when they are broadcast.
   This message will be sent to all users every time a user connects or disconnects from the
   server. |#


(define SendMessage (signature [ListOf (mixed Symbol String)]))
#| SendMessage: (list 'send String String): A message sent from a user.
   The first string is the users full name, the second string is the message they are sending   |#


(define Message (signature (mixed UserMessage SendMessage)))
;;A Message is either a UserMessage or SendMessage

;; A WorldState is a (make-world-state String String [ListOf String] [ListOf String] Boolean Number Number), where:
(define-struct world-state [msg msgs users blocked-users rate-limit? budget limit])
;; - msg is a string that represents a message written by the user
;; - msgs is list of strings that represents the messages sent and in the chatbox
;; - users is a list of strings that represent the names of all the users
;; - blocked-users is a list of strings that represents the names of blocked users
;; - rate-limit? is boolean representing if there is a rate limit present or not
;; - budget is a number representing the remaining messages available to send per second
;; - limit is a number representing the number of chats that can be received per second

(define AWorldState (signature [WorldStateOf String
                                             [ListOf String]
                                             [ListOf String]
                                             [ListOf String]
                                             Boolean
                                             Number
                                             Number]))

;;Examples

(define initial-state [make-world-state "" '() '() '() #f 4 4])

(define W1 (make-world-state "hello"
                             (list "Gabriela Nirmal: what's up" "Ahan Jain: nothing much")
                             (list "Gaby Nirmal" "Ahan Jain" "Mark Stevenson" "Snoop Dogg")
                             (list "Snoop Dogg")
                             #t
                             6
                             6))

(define W2 (make-world-state "where's the chees"
                             (list "Alicia Keys: I want to eat cheese and crackers" "John Lennon: Me too")
                             (list "Alicia Keys" "John Lennon" "Bradley Cooper" "Tom Holland" "Julia Fox")
                             (list "Tom Holland" "Julia Fox")
                             #f
                             9
                             9))

(define W3 (make-world-state "hey guysz"
                             '()
                             (list "Sapna Chudgar" "Julian Nirmal")
                             '()
                             #t
                             0
                             3))


;;Template

(define (world-state-temp ws)
  (cond
    [(world-state? ws) (... (world-state-msg ws) ... (world-state-msgs ws) ...
                       (world-state-users ws) ... (world-state-blocked-users ws) ...
                       (world-state-rate-limit? ws) ... (world-state-budget ws) (world-state-limit ws)...)]))


;; receive-handler ----------------------------------------------------------------------------------------------------------------------------------

(: receive-message (AWorldState Message -> AWorldState))
  #| receives messages from the server in the following forms:
   (list 'users [ListOf String]): A list of all connected client names.
   These are the same names as will be attached to messages when they are broadcast.
   This message will be sent to all users every time a user connects or disconnects from the
   server.

   (list 'send String String): A message sent from a user.
   The first string is the users full name, the second string is the message they are sending
  |#


(define (receive-message ws msg)
  (cond
    [(and (list? msg) (symbol? (first msg)) (symbol=? (first msg) 'users))
     (make-world-state (world-state-msg ws)
                       (world-state-msgs ws)
                       (second msg)
                       (world-state-blocked-users ws)
                       (world-state-rate-limit? ws)
                       (world-state-budget ws)
                       (world-state-limit ws))]
    
    [(and (list? msg) (symbol? (first msg)) (symbol=? (first msg) 'send))
     (make-world-state (world-state-msg ws)
                       (if (and (not (member? (second msg) (world-state-blocked-users ws))) (or (false? (world-state-rate-limit? ws)) (and (world-state-rate-limit? ws) (> (world-state-budget ws) 0))))
                           (if (> (length (world-state-msgs ws)) 23) ; it resets the message window to a clear screen after the number of messages exceeds 23, with the newest message on top
                               (list (string-append (second msg) ": "  (third msg)))  
                               (foldl (lambda (x r) (cons x r)) '() (cons (string-append (second msg) ": "  (third msg)) (reverse (world-state-msgs ws))))) ; adds the newest message at the bottom of the list
                           (world-state-msgs ws))
                       (world-state-users ws)
                       (world-state-blocked-users ws)
                       (world-state-rate-limit? ws)
                       (if (zero? (world-state-budget ws))
                           0
                           (sub1 (world-state-budget ws)))
                       (world-state-limit ws))]))


(check-expect (receive-message initial-state (list 'users (list "Gaby Nirmal" "Ahan Jain" "Mark Stevenson" "Ice Cube")))
              (make-world-state "" '() (list "Gaby Nirmal" "Ahan Jain" "Mark Stevenson" "Ice Cube") '() #f 4 4))

(check-expect (receive-message initial-state (list 'send "Gabriela Nirmal" "what's up"))
              (make-world-state "" (list "Gabriela Nirmal: what's up") '() '() #f 3 4))

(check-expect (receive-message W1 (list 'users (list "Ahan Jain" "Mark Stevenson" "Snoop Dogg" "Manish Nirmal")))
              (make-world-state "hello"
                                (list "Gabriela Nirmal: what's up" "Ahan Jain: nothing much")
                                (list "Ahan Jain" "Mark Stevenson" "Snoop Dogg" "Manish Nirmal")
                                (list "Snoop Dogg")
                                #t
                                6
                                6))

(check-expect (receive-message W1 (list 'send "Manish Nirmal" "hey everyone"))
              (make-world-state "hello"
                                (list "Gabriela Nirmal: what's up" "Ahan Jain: nothing much" "Manish Nirmal: hey everyone")
                                (list "Gaby Nirmal" "Ahan Jain" "Mark Stevenson" "Snoop Dogg")
                                (list "Snoop Dogg")
                                #t
                                5
                                6))


;;block check
(check-expect (receive-message W1 (list 'send "Snoop Dogg" "whats diggity danglin"))
              (make-world-state "hello"
                                (list "Gabriela Nirmal: what's up" "Ahan Jain: nothing much")
                                (list "Gaby Nirmal" "Ahan Jain" "Mark Stevenson" "Snoop Dogg")
                                (list "Snoop Dogg")
                                #t
                                5
                                6))


;;budget check
(check-expect (receive-message W3 (list 'send "Sapna Chudgar" "What do we want for dinner?"))
              (make-world-state "hey guysz"
                                '()
                                (list "Sapna Chudgar" "Julian Nirmal")
                                '()
                                #t
                                0
                                3))

             

;; key-handler ----------------------------------------------------------------------------------------------------------------------------------

(: send-message (AWorldState String -> (mixed AWorldState Package)))
;; send-message takes in AWorldState and Key
;; - if the key is \b, the delete space has been pressed and the last character written
;;      in the writing box will be removed
;; - if the key is \r, the return key has been pressed and the message will be sent in the form of a (make-package world-state your-message)
;; - if the key is a letter it will be appended to the string
;; - if the key is a special key like command shift etc, nothing will change 


(define (send-message ws a-key)
  (cond
    [(= (string-length a-key) 1)
     (cond
       [(key=? a-key "\b") (make-world-state (if (= (string-length (world-state-msg ws)) 0)
                                                 (world-state-msg ws)
                                                 (substring (world-state-msg ws) 0 (sub1 (string-length (world-state-msg ws)))))
                                             (world-state-msgs ws)
                                             (world-state-users ws)
                                             (world-state-blocked-users ws)
                                             (world-state-rate-limit? ws)
                                             (world-state-budget ws)
                                             (world-state-limit ws))]
    
       [(key=? a-key "\r") (make-package (make-world-state ""
                                                           (world-state-msgs ws)
                                                           (world-state-users ws)
                                                           (world-state-blocked-users ws)
                                                           (world-state-rate-limit? ws)
                                                           (world-state-budget ws)
                                                           (world-state-limit ws))
                                         (world-state-msg ws))]
       
       [else (make-world-state (string-append (world-state-msg ws) a-key)
                               (world-state-msgs ws)
                               (world-state-users ws)
                               (world-state-blocked-users ws)
                               (world-state-rate-limit? ws)
                               (world-state-budget ws)
                               (world-state-limit ws))])]
    [else ws]))


;;backspace check
(check-expect (send-message initial-state "\b")
              (make-world-state "" '() '() '() #f 4 4))

(check-expect (send-message W1 "\b")
              (make-world-state "hell"
                                (list "Gabriela Nirmal: what's up" "Ahan Jain: nothing much")
                                (list "Gaby Nirmal" "Ahan Jain" "Mark Stevenson" "Snoop Dogg")
                                (list "Snoop Dogg")
                                #t
                                6
                                6))
;;return check
(check-expect (send-message initial-state "\r")
              (make-package (make-world-state "" '() '() '() #f 4 4) ""))

(check-expect (send-message W1 "\r")
              (make-package (make-world-state ""
                                              (list "Gabriela Nirmal: what's up" "Ahan Jain: nothing much")
                                              (list "Gaby Nirmal" "Ahan Jain" "Mark Stevenson" "Snoop Dogg")
                                              (list "Snoop Dogg")
                                              #t
                                              6
                                              6)
                            "hello"))
;;letter check
(check-expect (send-message W2 "e")
              (make-world-state "where's the cheese"
                                (list "Alicia Keys: I want to eat cheese and crackers" "John Lennon: Me too")
                                (list "Alicia Keys" "John Lennon" "Bradley Cooper" "Tom Holland" "Julia Fox")
                                (list "Tom Holland" "Julia Fox")
                                #f
                                9
                                9))


;;non-letter check
(check-expect (send-message W3 "shift") W3)
(check-expect (send-message W2 "down") W2)
              



;; draw-handler helpers ----------------------------------------------------------------------------------------------------------------------------------

(: draw-users ([ListOf String] AWorldState -> Image))
(define (draw-users lou ws)
  ;; draw-users takes in a list of strings representing the list of users and AWorldState and returns an Image
  ;; representing the users
  (cond
    [(empty? lou) (empty-scene 0 0)]
    [else (above (if (member? (first lou) (world-state-blocked-users ws))
                     (overlay (text (first lou) 21 "red") (rectangle 300 20 "outline" "transparent"))
                     (overlay (text (first lou) 21 "black") (rectangle 300 20 "outline" "transparent"))) (rectangle 0 10 "solid" "transparent") (draw-users (rest lou) ws))]))


(check-expect (draw-users (list "Gaby Nirmal" "Ahan Jain") initial-state)
              (above (overlay (text "Gaby Nirmal" 21 "black") (rectangle 300 20 "outline" "transparent"))
                     (rectangle 0 10 "solid" "transparent")
                     (overlay (text "Ahan Jain" 21 "black") (rectangle 300 20 "outline" "transparent"))
                     (rectangle 0 10 "solid" "transparent")))

;block check
(check-expect (draw-users (list "Gaby Nirmal" "Ahan Jain" "Mark Stevenson" "Snoop Dogg") W1)
              (above (overlay (text "Gaby Nirmal" 21 "black") (rectangle 300 20 "outline" "transparent"))
                     (rectangle 0 10 "solid" "transparent")
                     (overlay (text "Ahan Jain" 21 "black") (rectangle 300 20 "outline" "transparent"))
                     (rectangle 0 10 "solid" "transparent")
                     (overlay (text "Mark Stevenson" 21 "black") (rectangle 300 20 "outline" "transparent"))
                     (rectangle 0 10 "solid" "transparent")
                     (overlay (text "Snoop Dogg" 21 "red") (rectangle 300 20 "outline" "transparent"))
                     (rectangle 0 10 "solid" "transparent")))

;;base case
(check-expect (draw-users '() initial-state)
              (empty-scene 0 0))
              

(: draw-messages ([ListOf String] -> Image))
(define (draw-messages lom)
  ;; draw-messages takes in a list of strings representing a list of messages and returns an image representing the messages
  (cond
    [(empty? lom) (empty-scene 0 0)]
    [else (above/align "left" (text (first lom) 21 "black") (rectangle 0 10 "solid" "transparent") (draw-messages (rest lom)))]))

;base case
(check-expect (draw-messages '()) (empty-scene 0 0))

(check-expect (draw-messages (list "Gabriela Nirmal: what's up" "Ahan Jain: nothing much"))
              (above/align "left" (text "Gabriela Nirmal: what's up" 21 "black") (rectangle 0 10 "solid" "transparent")
                           (text "Ahan Jain: nothing much" 21 "black") (rectangle 0 10 "solid" "transparent")))

(check-expect (draw-messages (list "Gabriela Nirmal: heyyyyyy"))
              (above/align "left" (text "Gabriela Nirmal: heyyyyyy" 21 "black") (rectangle 0 10 "solid" "transparent")))
                           

(define (interface ws)
  ; converts all components of the server into an image EXCEPT the lines that segregate the parts of the server (e.g. the headings, the list of users, the message space, the rate-limiter etc.)
  (overlay/xy (overlay/offset 
               (beside (rectangle 75 30 "solid" "transparent") (text "Active User(s)" 25 "black") (rectangle 590 30 "solid" "transparent") (text "Messages" 25 "black"))
               -320 (+ (/(image-height (draw-users (world-state-users ws) ws)) 2) 35)
               (draw-users (world-state-users ws) ws)) 
              330 50
              (draw-messages (world-state-msgs ws))))


(check-expect (interface initial-state)
              (overlay/xy (overlay/offset 
                           (beside (rectangle 75 30 "solid" "transparent") (text "Active User(s)" 25 "black") (rectangle 590 30 "solid" "transparent") (text "Messages" 25 "black"))
                           -320 (+ (/(image-height (draw-users (world-state-users initial-state) initial-state)) 2) 35)
                           (draw-users (world-state-users initial-state) initial-state)) 
                          330 50
                          (draw-messages (world-state-msgs initial-state))))

(check-expect (interface W1)
              (overlay/xy (overlay/offset 
                           (beside (rectangle 75 30 "solid" "transparent") (text "Active User(s)" 25 "black") (rectangle 590 30 "solid" "transparent") (text "Messages" 25 "black"))
                           -320 (+ (/(image-height (draw-users (world-state-users W1) W1)) 2) 35)
                           (draw-users (world-state-users W1) W1)) 
                          330 50
                          (draw-messages (world-state-msgs W1))))

(check-expect (interface W2)
              (overlay/xy (overlay/offset 
                           (beside (rectangle 75 30 "solid" "transparent") (text "Active User(s)" 25 "black") (rectangle 590 30 "solid" "transparent") (text "Messages" 25 "black"))
                           -320 (+ (/(image-height (draw-users (world-state-users W2) W2)) 2) 35)
                           (draw-users (world-state-users W2) W2)) 
                          330 50
                          (draw-messages (world-state-msgs W2))))
            

;; MAIN draw-handler ----------------------------------------------------------------------------------------------------------------------------------

(define USER-COLUMN 300) ; the width of the column that contains the Active User(s) heading, the list of users and the limit box
(define LOWER-LINE-LENGTH 795) ; the lower horizontal line that contains the limit box and the message box
(define HEADINGS-HEIGHT 40) ; the height of the row that contains the Active User(s) and Messages headings
(define TEXT-HEIGHT 25) ;  size of the (transparent) boxes that encapsulate messages

(: draw-chatroom (AWorldState -> Image)) 
(define (draw-chatroom ws)
  ; converts the server into an image, in the form of a chatroom, and segregates the parts of the server with lines
  ; parts of the server include:
  ; - the headings (Active User(s), Messages)
  ; - the list of users
  ; - the box which contains the rate limiter
  ; - the space where all messages are displayed 
  ; - the message box to type the message
  (overlay/xy (overlay/xy (overlay/xy (overlay/xy
                                       (interface ws)
                                       0 815
                                       (beside (if (world-state-rate-limit? ws)
                                                   (overlay (text "Limit: on" 23 "green") (rectangle 300 20 "outline" "transparent"))
                                                   (overlay (text "Limit: off" 23 "red") (rectangle 300 20 "outline" "transparent")))
                                                   
                                               (rectangle 30 20 "solid" "transparent")
                                               (text "Type here:" 23 "black")
                                               (rectangle 5 0 "solid" "transparent")
                                               (text (world-state-msg ws) 23 "black")))
                                      USER-COLUMN 0
                                      (line 0 850 "black"))
                          0 HEADINGS-HEIGHT
                          (line 1500 0 "black"))
              0 LOWER-LINE-LENGTH
              (line 1500 0 "black")))

(check-expect (draw-chatroom initial-state)
              (overlay/xy (overlay/xy (overlay/xy (overlay/xy
                                                   (interface initial-state)
                                                   0 815
                                                   (beside (if (world-state-rate-limit? initial-state)
                                                               (overlay (text "Limit: on" 23 "green") (rectangle 300 20 "outline" "transparent"))
                                                               (overlay (text "Limit: off" 23 "red") (rectangle 300 20 "outline" "transparent")))
                                                   
                                                           (rectangle 30 20 "solid" "transparent")
                                                           (text "Type here:" 23 "black")
                                                           (rectangle 5 0 "solid" "transparent")
                                                           (text (world-state-msg initial-state) 23 "black")))
                                                  USER-COLUMN 0
                                                  (line 0 850 "black"))
                                      0 HEADINGS-HEIGHT
                                      (line 1500 0 "black"))
                          0 LOWER-LINE-LENGTH
                          (line 1500 0 "black")))

(check-expect (draw-chatroom W3)
              (overlay/xy (overlay/xy (overlay/xy (overlay/xy
                                                   (interface W3)
                                                   0 815
                                                   (beside (if (world-state-rate-limit? W3)
                                                               (overlay (text "Limit: on" 23 "green") (rectangle 300 20 "outline" "transparent"))
                                                               (overlay (text "Limit: off" 23 "red") (rectangle 300 20 "outline" "transparent")))
                                                   
                                                           (rectangle 30 20 "solid" "transparent")
                                                           (text "Type here:" 23 "black")
                                                           (rectangle 5 0 "solid" "transparent")
                                                           (text (world-state-msg W3) 23 "black")))
                                                  USER-COLUMN 0
                                                  (line 0 850 "black"))
                                      0 HEADINGS-HEIGHT
                                      (line 1500 0 "black"))
                          0 LOWER-LINE-LENGTH
                          (line 1500 0 "black")))

(check-expect (draw-chatroom W2)
              (overlay/xy (overlay/xy (overlay/xy (overlay/xy
                                                   (interface W2)
                                                   0 815
                                                   (beside (if (world-state-rate-limit? W2)
                                                               (overlay (text "Limit: on" 23 "green") (rectangle 300 20 "outline" "transparent"))
                                                               (overlay (text "Limit: off" 23 "red") (rectangle 300 20 "outline" "transparent")))
                                                   
                                                           (rectangle 30 20 "solid" "transparent")
                                                           (text "Type here:" 23 "black")
                                                           (rectangle 5 0 "solid" "transparent")
                                                           (text (world-state-msg W2) 23 "black")))
                                                  USER-COLUMN 0
                                                  (line 0 850 "black"))
                                      0 HEADINGS-HEIGHT
                                      (line 1500 0 "black"))
                          0 LOWER-LINE-LENGTH
                          (line 1500 0 "black")))
              


;; mouse-handler ----------------------------------------------------------------------------------------------------------------------------------

(define MouseEvent (signature (enum "button-down" "button-up" "drag" "move" "enter" "leave")))
(: mouse-event (AWorldState Integer Integer MouseEvent  -> AWorldState))
(define (mouse-event ws posx posy click)
  ; enables the user to block someone by clicking on their name,
  ; and enable or disable the rate limit by clicking on the button in the bottom left corner
  (cond
    [(and (< posx USER-COLUMN) (< posy LOWER-LINE-LENGTH) (> posy (+ HEADINGS-HEIGHT 5))  (mouse=? click "button-down")) ; checks if the mouse is in the box which contains the list of users
     (local [(define i (floor (/ (- posy (+ HEADINGS-HEIGHT 7))  (+ TEXT-HEIGHT 2))))] ; this arithmetic expression generates the index of the list the mouse is on based on the y position of the mouse
       (make-world-state (world-state-msg ws)
                         (world-state-msgs ws)
                         (world-state-users ws)
                         (if (< i (length (world-state-users ws)))
                             (if (not (member? (list-ref (world-state-users ws) i) (world-state-blocked-users ws)))  ; if the user was blocked, the click unblocks them and vice-versa
                                 (cons (list-ref (world-state-users ws) i) (world-state-blocked-users ws))
                                 (remove (list-ref (world-state-users ws) i) (world-state-blocked-users ws)))
                             (world-state-blocked-users ws))
                         (world-state-rate-limit? ws)
                         (world-state-budget ws)
                         (world-state-limit ws)))]
    [(and (< posx USER-COLUMN) (> posy LOWER-LINE-LENGTH)  (mouse=? click "button-down")) ; if the person is in the bottom left box which contains the rate limit button
     (make-world-state (world-state-msg ws)
                       (world-state-msgs ws)
                       (world-state-users ws)                     
                       (world-state-blocked-users ws)
                       (if (false? (world-state-rate-limit? ws)) ; if the button was disabled, the click enables it and vice-versa
                           #t
                           #f)
                       (world-state-budget ws)
                       (world-state-limit ws))]
    [else ws]))

(check-expect (mouse-event initial-state 20 12 "button-down") (make-world-state "" '() '() '() #false 4 4))

(check-expect (mouse-event W1 20 12 "button-down")
              (make-world-state "hello" (list "Gabriela Nirmal: what's up" "Ahan Jain: nothing much")
                                (list "Gaby Nirmal" "Ahan Jain" "Mark Stevenson" "Snoop Dogg")
                                (list "Snoop Dogg") #true 6 6))
;; block check
(check-expect (mouse-event W1 40 50 "button-down") (make-world-state "hello"
                                                       (list "Gabriela Nirmal: what's up" "Ahan Jain: nothing much")
                                                       (list "Gaby Nirmal" "Ahan Jain" "Mark Stevenson" "Snoop Dogg")
                                                       (list "Gaby Nirmal" "Snoop Dogg") #true 6 6))

;; rate-limit box check
(check-expect (mouse-event W1 40 805 "button-down") (make-world-state "hello"
                                                                      (list "Gabriela Nirmal: what's up" "Ahan Jain: nothing much")
                                                                      (list "Gaby Nirmal" "Ahan Jain" "Mark Stevenson" "Snoop Dogg")
                                                                      (list "Snoop Dogg") #false 6 6))


;; tick-handler ----------------------------------------------------------------------------------------------------------------------------------

(: tick-handler (AWorldState -> AWorldState))
(define (tick-handler ws)
  ; resets the budget to the limit amount
  (make-world-state (world-state-msg ws)
                       (world-state-msgs ws)
                       (world-state-users ws)                     
                       (world-state-blocked-users ws)
                       (world-state-rate-limit? ws)
                       (world-state-limit ws)
                       (world-state-limit ws)))

(check-expect (tick-handler W3)
              (make-world-state "hey guysz"
                             '()
                             (list "Sapna Chudgar" "Julian Nirmal")
                             '()
                             #t
                             3
                             3))

(check-expect (tick-handler (receive-message W1 (list 'send "Snoop Dogg" "whats diggity danglin")))
              (make-world-state "hello"
                                (list "Gabriela Nirmal: what's up" "Ahan Jain: nothing much")
                                (list "Gaby Nirmal" "Ahan Jain" "Mark Stevenson" "Snoop Dogg")
                                (list "Snoop Dogg")
                                #t
                                6
                                6))

               

;; big-bang ----------------------------------------------------------------------------------------------------------------------------------

(: main (String -> AWorldState))
; main function that executes the chatroom
(define (main code)
  (big-bang initial-state
    (name code)
    (register "lambda-chat.dbp.io")
    (port 20000)
    (to-draw draw-chatroom)
    (on-receive receive-message)
    (on-tick tick-handler 1)
    (on-key send-message)
    (on-mouse mouse-event)))



