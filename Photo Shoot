#lang htdp/bsl
(require 2htdp/image)
(require 2htdp/universe)

; A PS (Photo Shoot) is one of:
; - (make-launch Number Interval)
(define-struct launch [photographer goal])
; - (make-flight Number Interval Posn)
(define-struct flight [photographer goal drone])
; Where:
;   - photographer represents a photographer's x-coordinate on the ground
;   - goal represents the range of the desired image to to be captured at the ground-level
;   - drone (if any) represents the drone's x/y position (with 0, 0 at the bottom left)
; All numbers are measured in pixels.
 
; An Interval is a (make-interval Number Number)
(define-struct interval [left right])
;Interpretation: and represents the leftmost and rightmost range of an interval in pixel coordinates (inclusive)

;Examples for Interval
(define INTERVAL1 (make-interval 100 140))
(define INTERVAL2 (make-interval 20 50))
(define INTERVAL3 (make-interval 25 85))
(define INTERVAL4 (make-interval 45 60))

;Examples for Photo Shoot
(define PS1 (make-launch 60 INTERVAL1))
(define PS2 (make-flight 130 INTERVAL2 (make-posn 55 40)))
(define PS3 (make-launch 120 INTERVAL3))
(define PS4 (make-flight 39 INTERVAL2 (make-posn 30 92)))

; Examples for launch
(define L1 (make-launch 23 INTERVAL3))
(define L2 (make-launch 31 INTERVAL4))

; Examples for flight
(define F1 (make-flight 10 INTERVAL3 (make-posn 28 39)))
(define F2 (make-flight 48 INTERVAL4 (make-posn 40 50)))

; Defining constants for the height and width of the background
(define WIDTH 180)
(define HEIGHT 120)

;Templates

#; (define (interval-temp i)
     ( ... (interval-left i) ... (interval-right i))) 

#;(define (launch-temp ps)
    ( ... (launch-photographer ps) ... (launch-goal ps)))

#;(define (flight-temp ps)
    ( ... (flight-photographer ps) ... (flight-goal ps) ... (flight-drone ps)))

#;(define (photoshoot-temp ps)
    (cond [(launch? ps) ... (launch-temp ps) ...]
          [(flight? ps) ... (flight-temp ps) ...]))



;; flip-coordinate: PS or Number -> Number
; returns the height of the drone with respect to (0,0) in the bottom left
(define (flip-coordinate ps)
  (cond
    [(integer? ps) (- HEIGHT ps)]
    [else
     (- HEIGHT (posn-y (flight-drone ps)))]))

(check-expect (flip-coordinate F1) 81)
(check-expect (flip-coordinate 45) 75)
(check-expect (flip-coordinate F2) 70)
(check-expect (flip-coordinate PS2) 80)
(check-expect (flip-coordinate PS4) 28)
  

;falling-drone : PS -> PS
;Given a Photo Shoot, if there is a drone, it will move it down by 1 pixel

(define (falling-drone ps)
  
  (cond [(launch? ps) ps]
        [(flight? ps)
         (make-flight (flight-photographer ps)
                      (flight-goal ps)
                      (make-posn (posn-x (flight-drone ps)) (- (posn-y (flight-drone ps)) 1)))]))

(check-expect (falling-drone L1) (make-launch 23 INTERVAL3))
(check-expect (falling-drone L2) (make-launch 31 INTERVAL4))
(check-expect (falling-drone F1) (make-flight 10 INTERVAL3 (make-posn 28 38)))
(check-expect (falling-drone F2) (make-flight 48 INTERVAL4 (make-posn 40 49)))




; launch-drone : PS -> PS
;Given a Photo Shoot, if there is no drone in flight, there will be drone placed in flight
; 20 pixels in the air and 15 pixels to the right of the photographer. If the drone is already in flight,
; nothing changes

(define (launch-drone ps)
  (cond
    [(flight? ps) 
     ps]
    [else
     (make-flight (launch-photographer ps)
                  (launch-goal ps)
                  (make-posn (+ 15 (launch-photographer ps))  (flip-coordinate (- HEIGHT 20))))]))

(check-expect (launch-drone L1)(make-flight 23 INTERVAL3 (make-posn 38 20)))
(check-expect (launch-drone F1 )(make-flight 10 INTERVAL3 (make-posn 28 39)))
(check-expect (launch-drone L2) (make-flight 31 INTERVAL4 (make-posn 46 20)))
(check-expect (launch-drone F2) (make-flight 48 INTERVAL4 (make-posn 40 50)))


; range_of_drone: drone -> Number
; calculates the amount of ground the drone can see on its left or right
(define (range_of_drone ps) (/(posn-y (flight-drone ps)) 2))

(check-expect (range_of_drone PS2) 20) 


;shoot-over?: PS -> Boolean
; checks if shoot is over, i.e. if the drone has crashed or captured the goal, and returns a boolean value accordingly.
(define (shoot-over? ps)
  (cond
    [(launch? ps) #false]
    
    [(flight? ps)
     (cond [(= (posn-y (flight-drone ps)) 0) #true]
    
           [(and (>= (range_of_drone ps) (- (interval-right (flight-goal ps)) (posn-x (flight-drone ps)))) 
                 (>= (range_of_drone ps) (- (posn-x (flight-drone ps)) (interval-left (flight-goal ps)))))
            #true]
           [else #false])]))

(check-expect (shoot-over? L1) #false)
(check-expect (shoot-over? PS2) #false)
(check-expect (shoot-over? F2) #true)
(check-expect(shoot-over? (make-flight 33 (make-interval 21 59) (make-posn 40 38))) #true)
(check-expect(shoot-over? (make-flight 77 (make-interval 40 60) (make-posn 50 22))) #true)
(check-expect(shoot-over? (make-flight 12 (make-interval 33 77) (make-posn 55 42))) #false)
(check-expect(shoot-over? (make-flight 21 (make-interval 72 100) (make-posn 80 30))) #false)



;draw-ps: PS -> Image
; draws the photo shoot
(define (draw-ps ps)
  (cond
    [(launch? ps) (draw-launch ps)]

    [(flight? ps) (draw-flight ps)]))

(check-expect (draw-ps L1) (place-image (rectangle (- (interval-right (launch-goal L1)) (interval-left (launch-goal L1))) 8 "solid" "seagreen")
                                        (/ (+ (interval-right (launch-goal L1)) (interval-left (launch-goal L1))) 2) ( - HEIGHT 4)
                                        (place-image (rectangle 10 12 "solid" "red")
                                                     (launch-photographer L1) (- HEIGHT 6)
                                                     (empty-scene WIDTH HEIGHT "blue"))))

(check-expect (draw-ps F1) (place-image (rectangle 15 10 "solid" "black")
                                        (posn-x (flight-drone F1)) (flip-coordinate F1)
                                        (place-image (rectangle (- (interval-right (flight-goal F1)) (interval-left (flight-goal F1))) 8 "solid" "seagreen")
                                                     (/ (+ (interval-right (flight-goal F1)) (interval-left (flight-goal F1))) 2) ( - HEIGHT 4)
                               
                                                     (place-image (rectangle 10 12 "solid" "red")
                                                                  (flight-photographer F1) ( - HEIGHT 6)
                                                                  (empty-scene WIDTH HEIGHT "blue")))))

; draw-launch : PS -> Image
; Helper function to draw a launch PS

(define (draw-launch ps)
  (place-image (rectangle (- (interval-right (launch-goal ps)) (interval-left (launch-goal ps))) 8 "solid" "seagreen")
               (/ (+ (interval-right (launch-goal ps)) (interval-left (launch-goal ps))) 2) ( - HEIGHT 4)
               (place-image (rectangle 10 12 "solid" "red")
                            (launch-photographer ps) (- HEIGHT 6)
                            (empty-scene WIDTH HEIGHT "blue"))))

(check-expect (draw-launch PS1)   (place-image (rectangle (- (interval-right (launch-goal PS1)) (interval-left (launch-goal PS1))) 8 "solid" "seagreen")
                                               (/ (+ (interval-right (launch-goal PS1)) (interval-left (launch-goal PS1))) 2) ( - HEIGHT 4)
                                               (place-image (rectangle 10 12 "solid" "red")
                                                            (launch-photographer PS1) (- HEIGHT 6)
                                                            (empty-scene WIDTH HEIGHT "blue"))))

(check-expect (draw-launch PS3) (place-image (rectangle (- (interval-right (launch-goal PS3)) (interval-left (launch-goal PS3))) 8 "solid" "seagreen")
                                             (/ (+ (interval-right (launch-goal PS3)) (interval-left (launch-goal PS3))) 2) ( - HEIGHT 4)
                                             (place-image (rectangle 10 12 "solid" "red")
                                                          (launch-photographer PS3) (- HEIGHT 6)
                                                          (empty-scene WIDTH HEIGHT "blue"))))



; draw-flight : PS -> Image
; Helper function to draw a flight PS

(define (draw-flight ps)
  (place-image (rectangle 15 10 "solid" "black")
               (posn-x (flight-drone ps)) (flip-coordinate ps)
               (place-image (rectangle (- (interval-right (flight-goal ps)) (interval-left (flight-goal ps))) 8 "solid" "seagreen")
                            (/ (+ (interval-right (flight-goal ps)) (interval-left (flight-goal ps))) 2) ( - HEIGHT 4)
                               
                            (place-image (rectangle 10 12 "solid" "red")
                                         (flight-photographer ps) ( - HEIGHT 6)
                                         (empty-scene WIDTH HEIGHT "blue")))))


(check-expect (draw-flight PS2)  (place-image (rectangle 15 10 "solid" "black")
                                              (posn-x (flight-drone PS2)) (flip-coordinate PS2)
                                              (place-image (rectangle (- (interval-right (flight-goal PS2)) (interval-left (flight-goal PS2))) 8 "solid" "seagreen")
                                                           (/ (+ (interval-right (flight-goal PS2)) (interval-left (flight-goal PS2))) 2) ( - HEIGHT 4)
                               
                                                           (place-image (rectangle 10 12 "solid" "red")
                                                                        (flight-photographer PS2) ( - HEIGHT 6)
                                                                        (empty-scene WIDTH HEIGHT "blue")))))

(check-expect (draw-flight PS4)  (place-image (rectangle 15 10 "solid" "black")
                                              (posn-x (flight-drone PS4)) (flip-coordinate PS4)
                                              (place-image (rectangle (- (interval-right (flight-goal PS4)) (interval-left (flight-goal PS4))) 8 "solid" "seagreen")
                                                           (/ (+ (interval-right (flight-goal PS4)) (interval-left (flight-goal PS4))) 2) ( - HEIGHT 4)
                               
                                                           (place-image (rectangle 10 12 "solid" "red")
                                                                        (flight-photographer PS4) ( - HEIGHT 6)
                                                                        (empty-scene WIDTH HEIGHT "blue")))))


; shoot-success? : PS -> Boolean
; Helper function to check whether the shoot was a success or not

(define (shoot-success? ps)
  (cond [(and (> (posn-y (flight-drone ps)) 0) (boolean=? (shoot-over? ps) #true))#true]
        [(= (posn-y (flight-drone ps)) 0) #false]
        ))


;last-scene: PS -> Image
;displays at the end of big-bang whether or not the shoot was a success
(define (last-scene ps)
  (if (shoot-success? ps) 
      (place-image (text "Success!" 15 "white")
                   (/ WIDTH 2) 20
                   (draw-flight ps))
 
      (place-image (text "Failed." 15 "white")
                   (/ WIDTH 2) 20
                   (draw-flight ps))))


;handle-keyevent: PS KeyEvent -> PS
;launches the drone and controls it using keys
(define (handle-keyevent ps a-key)
  (cond
    
    [(key=? a-key "l") (if(launch? ps) (launch-drone ps)ps)]
   
                        
   
    [(key=? a-key "up") (if (flight? ps)(make-flight
                                         (flight-photographer ps)
                                         (flight-goal ps)
                                         (make-posn (posn-x (flight-drone ps)) (+ (posn-y (flight-drone ps)) 5)))ps)]
                         
    [(key=? a-key "right")(if (flight? ps) (make-flight
                                            (flight-photographer ps)
                                            (flight-goal ps)
                                            (make-posn (+ (posn-x (flight-drone ps)) 5) (posn-y (flight-drone ps)))) ps)]
    [(key=? a-key "left")(if (flight? ps) (make-flight
                                           (flight-photographer ps)
                                           (flight-goal ps)
                                           (make-posn (- (posn-x (flight-drone ps)) 5) (posn-y (flight-drone ps))))ps)]
    [else ps]))
       


(check-expect (handle-keyevent PS2 "up" ) (make-flight 130 INTERVAL2 (make-posn 55 45)))
(check-expect (handle-keyevent F2 "right" ) (make-flight 48 INTERVAL4 (make-posn 45 50)))
(check-expect (handle-keyevent PS1 "l" ) (make-flight (launch-photographer PS1)
                                                      (launch-goal PS1)
                                                      (make-posn (+ 15 (launch-photographer PS1)) 20)))
(check-expect (handle-keyevent F1 "left" ) (make-flight 10 INTERVAL3 (make-posn 23 39)))
(check-expect (handle-keyevent PS3 "l" ) (make-flight (launch-photographer PS3)
                                                      (launch-goal PS3)
                                                      (make-posn (+ 15 (launch-photographer PS3)) 20)))



;main: PS -> Boolean
;animates the photo shoot
(define (main ps)
  (if (<= (posn-y (flight-drone 
                   (big-bang ps
                     [to-draw draw-ps]
                     [on-tick falling-drone 0.1]
                     [on-key handle-keyevent]
                     [stop-when shoot-over? last-scene])))0)#false #true))


(main PS1)
