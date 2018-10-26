# PhpClean - PhpStorm/IDEA plugin  

[![Build Status](https://travis-ci.com/funivan/PhpClean.svg?branch=master)](https://travis-ci.com/funivan/PhpClean)

Code analyzers.
## List of inspection:

### MethodShouldBeFinal
Public methods should be closes (make method or class final)

### MethodVisibility 
Method should be `private` or `public`

### MissingParameterTypeDeclaration 
Always specify parameter type

### PhpCleanUndefinedMethod (experimental)
Try to fix undefined method phpstorm bugs: https://youtrack.jetbrains.com/issue/WI-5223

### PropertyAnnotation
Properties that are not initialized in the constructor should be annotated as nullable.
