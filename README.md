# PhpClean - PhpStorm/IDEA plugin  

[![Build Status](https://img.shields.io/travis/com/funivan/PhpClean.svg?style=flat-square)](https://travis-ci.com/funivan/PhpClean)
[![Version](https://img.shields.io/jetbrains/plugin/v/11272.svg?style=flat-square)](https://plugins.jetbrains.com/plugin/11272-phpclean)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/11272.svg?style=flat-square)](https://plugins.jetbrains.com/plugin/11272-phpclean)
[![License](https://img.shields.io/github/license/funivan/PhpClean.svg?style=flat-square)](LICENSE.md)



Static Code Analysis for PhpStorm and Intellij Idea.

## Installation
#### Stable builds
Open IDE go to `Settings->Plugins->Marketplace` search for the `PhpClean`.
Hit `install` button. Restart your IDE.

#### Nightly build - get fresh version after each commit
You can install nightly builds using custom repository.
 
Open IDE go to `Settings->Plugins->Manage plugin repositories` and add repository
`http://funivan.com/ci/PhpClean-nightly.xml`
Then you can install **PhpClean**


![GitHub commits (since latest release)](https://img.shields.io/github/commits-since/funivan/PhpClean/latest.svg?style=flat-square)



## List of inspection:
<!-- inspections -->
#### DeprecatedDocTag 
You can deprecate some PhpDoc tags in your project.
#### GlobalVariableUsage 
This inspection detects usages of global variables.
```php
echo $_GET['name']; // <-- Deprecated global variable usage
```
#### MethodCanBePrivate 
Protected methods can be converted to private.
```php
final class User {
  protected function name() {} // <-- Method can be private
}
```
#### MethodShouldBeFinal 
Methods should be closed (make method or class final)
```php
class User {
 public function name() : string { // <-- Method should be final
   return "";
 }
}
```
#### MethodVisibility 
Protected methods make our classes more open. Write private or public methods only.
#### MissingParameterTypeDeclaration 
Always specify parameter type. This is a good practice.
```php
class User{
 function withName($name){}  // <-- Missing parameter type
}
```
#### MissingReturnType 
Always specify result type of the function.
```php
function phrase(){ // <-- Missing return type
    return 'hi';
}
```
#### PhpCleanUndefinedMethod 
<b>Experimental</b> Try to fix undefined method phpstorm bugs: https://youtrack.jetbrains.com/issue/WI-5223
```php
class Email {
  public function send(){ }
}
/** @var mixed|Email $email */
$email->snd(); // <-- Undefined method
$email->send();
```
#### PropertyAnnotation 
Properties that are not initialized in the constructor should be annotated as nullable.
```php
class User {
 /** @var string */ // <-- Property is not annotated correctly. Add null type
 private $name;
 public function getName(){  }
 public function setName(string $name){  }
}
```
#### RedundantDocCommentTag 
Types that are specified in the php can be omitted in the PhpDoc blocks<br>
```php
/**
 * @return void // <-- Redundant PhpDoc tag
 */
 function show(string $message):void {}
```
#### ToStringCall 
Detect automatic type casting
```php
class Hello {
    public function randomize(): self { /* .. */return $this; }
    public function __toString(){ return 'Hi'; }
}
echo (new Hello())->randomize(); // <-- Deprecated __toString call
```
#### VirtualTypeCheck 
Use assert to check variable type instead of doc comment.
```php
/** @var $user User */ // <-- Use assert to check variable type
assert($user instanceof User);
```
