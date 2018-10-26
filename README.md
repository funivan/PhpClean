# PhpClean - PhpStorm/IDEA plugin  

[![Build Status](https://travis-ci.com/funivan/PhpClean.svg?branch=master)](https://travis-ci.com/funivan/PhpClean)

Code analyzers.
## List of inspection:

### MethodCanBePrivate
Public methods should be closes (make method or class final)

### MethodShouldBeFinal
Public methods should be closed (make method or class final)

### MethodVisibility 
Protected methods make our classes more open. Write private or public methods only.

### MissingParameterTypeDeclaration 
Always specify parameter type. This is a good practice.

### PhpCleanUndefinedMethod (experimental)
Try to fix undefined method phpstorm bugs: https://youtrack.jetbrains.com/issue/WI-5223

### PropertyAnnotation
Properties that are not initialized in the constructor should be annotated as nullable.