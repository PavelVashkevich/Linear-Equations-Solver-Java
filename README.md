Linear-Equations-Solver
-----------------------------------

This program allow you to solve linear equations system with simple numbers or Complex numbers by _Gauss-Jordan Elimination_.
 
The general system of linear equations looks like in the picture. The variables are named **x**.The coefficients are named **a** and the constants are named **b**.

![alt text](https://ucarecdn.com/b7d86b16-630c-4b81-bf0a-64c57db8edce/)

This program reads coefficients from a file, solves the system of linear equations and writes the answer to another file. You should pass paths to files using command-line arguments.

## Output example

Suppose you have a file named `in.txt`. It contains the following:

```
1 1 2 9
2 4 -3 1
3 6 -5 0
```

Below is how this program work. The lines which start with > represent the user input.

```
> java Solver -in in.txt -out out.txt
Start solving the equation.
Rows manipulation:
-2 * R1 + R2 -> R2
-3 * R1 + R3 -> R3
0.5 * R2 -> R2
-3 * R2 + R3 -> R3
-2 * R3 -> R3
3.5 * R3 + R2 -> R2
-2 * R3 + R1 -> R1
-1 * R2 + R1 -> R1
The solution is: (1.0, 2.0, 3.0)
Saved to file out.txt
```

### Important:
Program can also solve Complex numbers. Suppose you have a file named `in.txt`. It contains the following.

```
1+2i -1.5-1.1i 2.12 91+5i
-1+3i 1.2+3.5i -3.3 1+15i
12.31 1.3-5i 12.3i -78.3i
```

Below is output of this program

```
> java Solver -in in.txt -out out.txt
Start solving the equation.
Rows manipulation:
R1 / 1+2i -> R1
1-3i * R1 + R2 -> R2
-12.31 * R1 + R3 -> R3
R2 / 1.6+6.1i -> R2
-10.40+9.67i * R2 + R3 -> R3
R3 / -6.78+9.71i -> R3
0.54-0.74i * R3 + R2 -> R2
-0.42+0.84i * R3 + R1 -> R1
0.74-0.38i * R2 + R1 -> R1
The solution is: (6.73-22.99i, -1.79+2.0i, 15.6994+7.396i)
Saved to file out.txt
```

### Note

Equation can has `no solution` or  `Infinitely many solutions`. For example:

1. The number of significant equations is less than the number of significant variables. There is infinitely many solutions. The possible examples are shown below:
 
 ![alt text](https://ucarecdn.com/4afce670-32f7-4cc8-b75e-6fed3d57ee5a/)

2. No solution is possible when there is a fill-zero row and a constant is not equal to zero.
 
 ![alt text](https://ucarecdn.com/86e8a527-95ca-49ec-81d3-6b7ff6b07140/)
 
 Program write to file only `No solutions` or `Infinitely many solutions` when this appears to happen.