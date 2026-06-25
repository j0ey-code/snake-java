## Classic "Snake Game" in Core Java JDK 21+

### A simple recreation of the "snake game" using core Java GUI libraries

### First written and compiled with the core Java 21 JDK; included .jar file is a JDK 21 compiled .jar

j0ey-code - older project, originally completed during spring semester of 2024

### Description 

A basic implementation and clone of the quintessential "snake game". 
Extraneous IDE files removed, and a .jar file included within out/ directory for quicker execution upon repository pull / download.
Completed after light usage of the Java Swing libraries during the GUI component of my community college's Computer Science I course.
After learning the fundamentals of the Java Swing libraries that semester, w/ the help of Google / the Internet, I re-created the classic "snake game".

**Core Features::**
1. Java Swing libraries
2. Graphical rendering via the paintComponent
3. Game looping mechanism(s)
4. Event handling

**Additional Features::**
- Inherent restart mechanic
- Buffered image grid caching to improve performance / decrease lag

**2026 Fixes / Additions::**
- Removed the possibility for apple to spawn outside grid boundary entirely
- Removed the possibility for apple to spawn inside the snake's body
- Added simple "restart game" mechanic

### Build and Execution Instructions

Ensure you are running the Java 21 JDK or newer, or that you compile the src/ directory files with your own javac / IDE compiler. 
Ideally, a 17+ or 18+ Java JDK compiler would be best I'd imagine. 
Should run equally well, more or less, across various operating systems. 
Linux based OSes may experience noticeable lag / performance degradation in the game due to different display rendering, however.
Performance may also vary depending on what javac compiler version you use to create a .jar / executable, and your hardware (obviously).

