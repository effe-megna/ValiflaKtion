# ValiflaKtion

:crystal_ball:  A model **validation** library, written in Kotlin, that use annotations and **reflection**.

## Installation

Work in progress

## ⭐️ Features
* built-in validation rules like blank, email, min lenght, no numbers, etc.
* Multiple Validations and Checks
* Validation callback
* Create your own custom rules
* Respect of your types

## Why
The reason behind this library are explained in the followed article


## 🐎 Quick Usage

Define your model.

```kotlin
class User {
     @NotBlank("I can't have an empty fullname")
     val fullName: String

     @Email
     val email: String

     @AtLeastOneSpecialCharacter
     @AtLeastOneUpperCase
     @AtLeastOneNumberCase
     val password: String

     @IntegerGreaterThen(16)
     val Age: Int
  }
```
Pass an instance of your model to the validate method of Validator class.
```kotlin
validation = Validator.validate(myNewUser)
```

Check the results.
```kotlin
if (validation.isValidModel) {
  print("Hello world from ${user.fullName}")
} else {
  validator.constraintViolations["fullName"].rulesViolated.first().message
}

```
## 📖 Doc
* Built-in rules
  - [String](#String-rules)
  - [Int](#Int-rules)
  - [Boolean](#Boolean-rules)
* [Selector](#Selector)
* [Callback validation](#Callback-validation)
* [Define your own custom rule](#Custom-rule)
* Additional info

### String rules
| Rule  | Annotation | ErrorMessage (default) |
| ------------- | ------------- | ---------- |
| AllLowerCaseRule  | AllLowerCase | All letters should be in lower case. |
| AllUpperCaseRule| AllUpperCase |  All letters should be in upper case. |
| AtLeastOneLowerCaseRule | AtLeastOneLowerCase | At least one letter should be in lower case. |
| AtLeastOneNumberCaseRule | AtLeastOneNumberCase | At least one letter should be a number. |
| AtLeastOneSpecialCharacterRule | AtLeastOneSpecialCharacter | At least one letter should be a special character. |
| AtLeastOneUpperCaseRule | AtLeastOneUpperCase | At least one letter should be in upper case. |
| EmailRule | Email | Invalid Email address. |
| MinLengthRule | MinLength | Length should be greater than target |
| NoNumbersRule | NoNumbers | Should not contain any numbers. |
| NotBlankRule | NotBlank | Can't be empty. |
| RegexRule | Regex | Regex pattern doesn't match. |


### Int rules
| Rule  | Annotation | ErrorMessage (default) |
| ------------- | ------------- | ---------- |
| IntBetweenRule| IntBetween| The number should be between the lower and the greater. |
| IntLessThenRule | IntLessThen |The value should be less then the target. |


### Boolean rules
| Rule  | Annotation | ErrorMessage (default) |
| ------------- | ------------- | ---------- |
| AssertTrueRule| AssertTrue| The boolean should be true. |
| AssertFalseRule| AssertFalse| The boolean should be false. |


### Callback validation
Provide an object, implementing INotifier interface, to the Validator for callback through the validation process.
```kotlin
Validator.validate(myModel, object : INotifier {
    override fun validationSuccess(propertyName: String, annotations: List<Annotation>) {
       // write your logic
    }

     override fun validationFail(propertyName: String, violations: List<RuleViolated>) {
       // write your logic
    }

    override fun stopValidation(): Boolean {
       // return true for stop the entire process
    }
})
```

### Selector
The library contain an interface called `ISelector<T, R>`, useful for apply Rules in nested structure.

When use data binding in Android, i generally use the Observable type, this type exhibit an inner field containing the string, boolean, ecc..
With Selector i can target the value and apply rules on that.
```kotlin
class ObservableUser(

    @Selector(ObservableFieldSelector::class)
    @NotBlank
    val fullName: ObservableField<String>,
    
    @Selector(ObservableFieldSelector::class)
    @Email
    val email: ObservableField<String>,
    
    @Selector(ObservableBooleanSelector::class)
    @AssertTrue
    val isObservable: ObservableBoolean   
)
```

Implementing the selectors class.
The first param in `ISelector<T, R>` is the type of your property, the second param is the type where the rules are applied.
```kotlin
class ObservableFieldSelector : ISelector<ObservableField<String>, String> {
        override fun extractValueToValidate(value: ObservableField<String>): String? {
                return value.get()
        }
}

class ObservableBooleanSelector : ISelector<ObservableBoolean, Boolean> {
        override fun extractValueToValidate(value: ObservableBoolean): Boolean {
                return value.get()
        }
}
```
With Selector you can target nested field 

### Custom rule
Define your custom rule on your preferred type in 3 steps.

1. #### Example with StartsWithRule. Define a class and implement IRule<String>.
```kotlin
class StartsWithRule(
        val target: String
) : IRule<String> {

    @Target(AnnotationTarget.PROPERTY)
    annotation class StartsWith(val target: String) // Annotation for identify our rule

    override fun isValid(value: String?): Boolean { // your validation logic
        return value?.startsWith(target) ?: false
    }
}
```
You can construct a rule as you want, in this case we need a target for check if the value starts with it.
Pass what you need for construct the rule through the annotation. [Type supported](https://kotlinlang.org/docs/reference/annotations.html#constructors)

2. #### Provide a Builder for the rule, usually situated into the Rule class.
```kotlin
 companion object Builder : IRuleBuilder<String> {
 
    override fun buildFromAnnotation(annotation: Annotation): IRule<String>? {
        return if (annotation is StartsWith) {
          StartsWithRule(annotation.target)
        } else null
    }
  }
```

3. #### Create a validator instance and add your custom RuleBuilder to rulesBuilder list.
```kotlin
  val validator = Validator(myNewUser)

  validator.ruleBuilders.add(StartsWithRule.Builder)

  validator.executeValidation()
```
  And tada! 🎉,  your custom rule is now ready.



## 🚶 Developed By

```
Francesco Megna
```
- [LinkedIn](https://www.linkedin.com/in/francesco-megna-19a266162)

# 👍 How to Contribute
1. Fork it
2. Create your feature branch (git checkout -b my-new-feature)
3. Commit your changes (git commit -am 'Add some feature')
4. Push to the branch (git push origin my-new-feature)
5. Create new Pull Request

# 👷 Roadmap
* Expand built-in rules
* Write more tests
* Doc improvements
* Formatted commit using tools like Committizez/cz-cli
* Integration with android `EditText`, `TextView`, `AutoCompleteTextView`, `TextInputLayout`, ecc.
* Explore the reflection API

# 📃 License

    MIT License
    
    Copyright (c) 2019 Francesco Megna
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.