# SimpleTableEditor

SimpleTableEditor is a lightweight Java-based application designed to facilitate the editing of tabular data with embedded formula support. The application leverages the Swing tableModelwork to provide a straightforward user interface for table manipulation directly within cell inputs, without the need for a separate formula input field.

## Features


SimpleTableEditor supports a range of formula capabilities that allow users to perform complex data manipulations directly within the table cells. Below, we detail the types of operations and functions available.

### 1. Parentheses for Controlling Operation Precedence
Parentheses are used to override the default precedence of operations, ensuring that operations enclosed in them are computed first. This feature is essential for creating complex expressions that yield accurate results.

### 2. Binary and Unary Operators
The editor supports the following operators to perform arithmetic and logical operations:

- **Binary Operators:**
    - `+` (Addition)
    - `-` (Subtraction)
    - `*` (Multiplication)
    - `/` (Division)

- **Unary Operators:**
    - `-` (Negation): Changes the sign of the operand.

### 3. Named Functions
A variety of named functions are available, ranging from trigonometric and logarithmic functions to statistical operations:

- **Trigonometric Functions:**
    - `SIN(x)`
    - `COS(x)`
    - `TAN(x)`

- **Logarithmic and Exponential Functions:**
    - `LOG(x)`
    - `EXP(x)`
    - `LOG10(x)`

- **Rounding Functions:**
    - `ROUND(x)`
    - `CEIL(x)`
    - `FLOOR(x)`

- **Arithmetic Functions:**
    - `SQRT(x)`
    - `ABS(x)`
    - `POW(x, y)`
    - `MOD(x, y)`

- **Statistical Functions:**
    - `MIN(x1, x2, ..., xn)`
    - `MAX(x1, x2, ..., xn)`
    - `SUM(x1, x2, ..., xn)`
    - `AVERAGE(x1, x2, ..., xn)`

- **Conversion Functions:**
    - `DEGREES(x)`
    - `RADIANS(x)`
    - `SIGNUM(x)`

### 4. References to Other Cells Within the Table
Formulas can reference other cells dynamically, enabling real-time data manipulation based on values from different parts of the table. The application manages these references using a dependency graph, preventing issues such as circular references.

Each formula feature is designed to provide maximum flexibility and control to the user, ensuring that SimpleTableEditor can handle a variety of data manipulation tasks effectively.

### 5. Undo Functionality

The editor includes undo functionality that allows users to revert their actions to a previous state, enhancing the flexibility and usability of the table manipulation process. This feature is crucial for managing mistakes and refining table data without having to manually reverse each change.

Each formula feature is designed to provide maximum flexibility and control to the user, ensuring that SimpleTableEditor can handle a variety of data manipulation tasks effectively.

## Implementation Details

### Design Patterns and Architecture

SimpleTableEditor employs two main design patterns to ensure a robust and scalable application architecture:

- **Model-View-Controller (MVC):** This pattern divides the application into three interconnected components, improving separation of concerns and promoting modular development.
  - **Model (`TableModel`):** Manages the underlying data structure and logic of the spreadsheet, including cell values, dimensions, and label management. It acts independently of the user interface and handles data-related operations.
  - **View (`TableView`):** Responsible for all user interface components, presenting the table structure and cell data. It renders the spreadsheet cells that users interact with and reflects any changes made to the Model.
  - **Controller (`TableController`):** Acts as an intermediary between the View and the Model, handling user input and manipulating the data model accordingly. It manages cell selection, updates based on user edits, and invokes undo operations.

- **Memento Pattern:** Utilized to implement the undo functionality that allows users to revert to previous states of the spreadsheet.
  - **Memento (`TableModelSnapshot`):** Captures and externalizes the internal state of a `TableModel` at a given moment without exposing its implementation details. This snapshot includes cell values and dependencies.
  - **Caretaker (`TableController`):** Manages the memento's lifecycle. It keeps track of the history of states using a stack and provides mechanisms to restore any previously saved state.

### Detailed Components

- **UI Components:**
  - **`TableModel`:** Core component of the MVC model, managing spreadsheet dimensions, cell values, and label configurations. It ensures data integrity and consistency throughout operations.
  - **`TableView`:** Visual representation in the MVC architecture. It sets up GUI components like the table grid and editors, reacting to model updates and user interactions.
  - **`TableController`:** Facilitates communication between the model and view, handling event-driven interactions such as cell editing and selection, and managing the undo stack for state reversals.

- **Expression Evaluation:**
  - **`Expr` Interface and Implementations:** Defines a family of expression types (`NumberExpr`, `BinaryExpr`, `FunctionExpr`, `CellExpr`) that the parser translates user inputs into. These objects represent different types of calculations and operations within cells.
  - **`Parser`:** Converts user input strings into structured `Expr` objects forming an expression tree. This parser supports arithmetic operations, function calls, and dynamic cell references, enabling complex formula evaluations.

- **Dependency Management:**
  - **`CellDependencyGraph`:** Manages cell relationships to track how changes to one cell affect others. It prevents cycles in dependencies, ensuring stable updates across the spreadsheet without recursion or errors.

## Setup

- **Requirements:** Java 23
- **IDE:** Tested on IntelliJ IDEA
- **Execution:** Run `SimpleTableEditor` with arguments `<width>`, `<height>`, `<screen width>`, `<screen height>` and `<max history>` to define the table dimensions.
- **Tested on:** macOS with Java 23 and IntelliJ IDEA
