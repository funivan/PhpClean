# PhpClean - PhpStorm/IDEA plugin  

[![Build Status](https://travis-ci.com/funivan/PhpClean.svg?branch=master)](https://travis-ci.com/funivan/PhpClean)

Static Code Analysis for PhpStorm and Intellij Idea.

## List of inspection:

### MethodCanBePrivate
Protected methods can be converted to private.

### MethodShouldBeFinal
Public methods should be closed (make method or class final)

### MethodVisibility 
Protected methods make our classes more open. Write private or public methods only.

### MissingReturnTypeInspection 
Always specify result type of the function.

### MissingParameterTypeDeclaration 
Always specify parameter type. This is a good practice.

### PhpCleanUndefinedMethod (experimental)
Try to fix undefined method phpstorm bugs: https://youtrack.jetbrains.com/issue/WI-5223

### PropertyAnnotation
Properties that are not initialized in the constructor should be annotated as nullable.

## Installation
You can install nightly builds using custom repository.
 
Open IDE go to `Settings->Plugins->Manage plugin repositories` and add repository
`http://funivan.com/ci/PhpClean-nightly.xml`
Then you can install **PhpClean**