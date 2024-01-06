# PhpClean - PhpStorm/IDEA plugin  

[![Version](https://img.shields.io/jetbrains/plugin/v/11272.svg?style=flat-square)](https://plugins.jetbrains.com/plugin/11272-phpclean)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/11272.svg?style=flat-square)](https://plugins.jetbrains.com/plugin/11272-phpclean)
[![License](https://img.shields.io/github/license/funivan/PhpClean.svg?style=flat-square)](LICENSE.md)



Static Code Analysis for PhpStorm and Intellij Idea.

## Installation
Open IDE go to `Settings->Plugins->Marketplace` search for the `PhpClean`.
Hit `install` button.

![GitHub commits (since latest release)](https://img.shields.io/github/commits-since/funivan/PhpClean/latest.svg?style=flat-square)

[- Inspections](#Inspections)

[- Actions](#Actions)


<!-- Autogenerated -->
## Inspections
#### AssignMisusedInspection.html
Detects assignment and comparison operators in one statement.
```php
while (false !== $current = ldap_next_entry($con, $current)) {
  // ^^^ Hard to read this statements
  yield $this->getSingleEntry($con, $current);
}
```
#### ClassNameCollisionInspection.html
Classes with same name in different namespaces can be confused.
(Disabled by default)
```php
namespace App {
  class User {}; // <- Class name collision with \Cli\User
}
namespace Cli {
  class User {}; // <- Class name collision with \App\User
}
```
#### DeprecatedDocTagInspection.html
You can deprecate some PhpDoc tags in your project.
#### GlobalVariableUsageInspection.html
This inspection detects usages of global variables.
```php
echo $_GET['name']; // <-- Global variable usage
```
#### MethodCanBePrivateInspection.html
Protected methods can be converted to private.
```php
final class User {
  protected function name() {} // <-- Method can be private
}
```
#### MethodShouldBeFinalInspection.html
Methods should be closed (make method or class final)
```php
class User {
 public function name(): string { // <-- Method should be final
   return '';
 }
}
```
#### MethodVisibilityInspection.html
Protected methods make our classes more open. Write private or public methods only.
#### MissingParameterTypeDeclarationInspection.html
Always specify parameter type. This is a good practice.
```php
class User {
 public function withName($name) {}  // <-- Missing parameter type
}
```
#### MissingReturnTypeInspection.html
Always specify result type of the function.
```php
function phrase() { // <-- Missing return type
    return 'hi';
}
```
#### ParentPropertyDeprecatedInspection.html
Check if parent property is deprecated.
```php
  class A {
    /** @deprecated */
    protected $name;
  }
  class B extends A {
    protected $name; // <-- Warn about deprecation
  }
```
#### ProhibitedClassExtendInspection.html
Classes marked with `@final` doc tag should not be extended
```php
/**
 * @final
 */
 class User {};

 class Admin extends User {}; // <- Prohibited extentions of @final class User.
```
#### PropertyAnnotationInspection.html
Properties that are not initialized in the constructor should be annotated as nullable.
```php
class User {
 /** @var string */ // <-- Property is not annotated correctly. Add null type
 private $name;
 public function getName() {  }
 public function setName(string $name) {  }
}
```
#### PropertyCanBePrivateInspection.html
Protected properties can be converted to private.
```php
class User {
  protected $user; // <-- Property can be private
}
```
#### RedundantDocCommentTagInspection.html
Types that are specified in the php can be omitted in the PhpDoc blocks<br>
```php
/**
 * @return void // <-- Redundant PhpDoc tag
 */
 function show(string $message): void {}
```
#### ToStringCallInspection.html
Detect automatic type casting
```php
class Hello {
    public function randomize(): self { /* ... */return $this; }
    public function __toString() { return 'Hi'; }
}
echo (new Hello())->randomize(); // <-- Deprecated __toString call
```
#### VirtualTypeCheckInspection.html
Use assert to check variable type instead of doc comment.
```php
/** @var User $user */ // <-- Use assert to check variable type
assert($user instanceof User);
```

## Actions
#### UseNamedConstructorAction.html
Replace `new ClassName()` with selected named constructor.

```php
class Text {
 public function __construct(string $name){ }
 public static fromName(string $n){}
}
```
Invoke `refactor this` on method name `fromName`
and all new statements with this class will be changed

```php
 new Text('User') // old code
 Text::fromName('User') // new code
```

## Intentions
#### UnimportClassIntention
<html>
<body>
Unimport class. Reverse operation of import (or simplify fqn) class name.
</body>
</html>

