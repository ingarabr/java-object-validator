Validation
==========

This is a library used to validate java object against a set of
validation rules.

This is inspired by mockito and hamcrest so it should be familiar
to use if you know those tools.

Limitations
-----------

It requires that the objects follow the java bean convention with
a field and a getter is present.


How to use
----------

build the project and import it with maven.

```xml
<dependency>
  <groupId>com.github.ingarabr.validator</groupId>
  <artifactId>validation</artifactId>
  <version>{validation.version}</version>
</dependency>
```

Building up a rule
------------------

First we need to specify the field we need:

```java
field(SomeBean.class).getValue();
```

Now we can add a validation condition to it:

```java
validation(field(SomeBean.class).getValue(), mustHaveValue());
```

This can be chain into other validation conditions like `and`, `or` etc.

To verify that an object is valid against the validation condition the API
expose two method. `isValidateObject` that gives a boolean result
and `validateObject` that gives a set of violations. The set it empty if
no validation condition is violated.

```java
Validation validation = validation(field(SomeBean.class).getValue(), hasOneOfTheValues("foo1", "foo2"));
boolean isValid = fieldCondition.isValidateObject(new SomeBean().value("foo1"));
```

See the test for more example of usage.
