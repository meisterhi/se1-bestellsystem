# C3: *JUnit* Tests for *Customer* Class

The assignment demonstrates writing *JUnit* tests for class *Customer*.

The *Test Plan* for class *Customer* is structured in categories:

1. *Constructor* (100) Tests in file:
    `Customer_100_Constructor_Tests.java` in package `datamodel` in `tests`.

1. *Id* (200) Tests in file:
    `Customer_200_Id_Tests.java` in same package.

1. *Name* (300) Tests in file:
    [Customer_300_Name_Tests.java](Customer_300_Name_Tests.java)
    in the same package.

1. *Contacts* (400) Tests in file:
    [Customer_400_Contacts_Tests.java](Customer_400_Contacts_Tests.java)
    in the same package.

1. Extended *Name* (500) Tests in file:
    [Customer_500_NameXXL_Tests.java](Customer_500_NameXXL_Tests.java)
    in the same package.


Topics:

1. [Create *Constructor* (100) Tests](#1-create-constructor-100-tests)
1. [Create *Id* (200) Tests](#2-create-id-200-tests)
1. [Run *Name* (300) Tests](#3-run-name-300-tests)
1. [Run *Contacts* (400) Tests](#4-run-contacts-400-tests)
1. [Run Extended *Name* (500) Tests](#5-run-extended-name-500-tests)
1. [Run all *Customer* Tests](#6-run-all-customer-tests)


&nbsp;

## 1. Create *Constructor* (100) Tests

Constructor tests test the constructor(s) of a class. Class *Customer*
has two constructors:

```java
/**
 * Default constructor.
 */
public Customer() { }

/**
 * Constructor with single-String name argument.
 * @param name single-String Customer name, e.g. "Eric Meyer"
 * @throws IllegalArgumentException if name argument is null
 */
public Customer(String name) {
    setName(name);  // throws IllegalArgumentException when name is null
}
```

Create a new test class in package `datamodel` in `tests` with
test methods for the default constructor:

```java
/**
 * Tests for {@link Customer} class: [100..199] with tested Constructors:
 * <pre>
 * - Customer()             // default constructor
 * - Customer(String name)  // constructor with name argument
 * </pre>
 * @author sgra64
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Customer_100_Constructor_Tests {

    /*
     * Regular test case 100: Default Constructor.
     */
    @Test @Order(100)
    void test100_DefaultConstructor() {
        final Customer c1 = new Customer();     // call default Constructor
        assertEquals(null, c1.getId());         // returns null for unassigned id
        assertEquals("", c1.getLastName());     // lastName: ""
        assertEquals("", c1.getFirstName());    // firstName: ""
        assertEquals(0, c1.contactsCount());    // 0 contacts
    }

    /*
     * Regular test case 101: Default Constructor test methods
     * are chainable.
     */
    @Test @Order(101)
    void test101_DefaultConstructorChainableSetters() {
        final Customer c1 = new Customer();
        // test self-reference is returned for setter methods
        assertSame(c1, c1.setId(0L));
        assertSame(c1, c1.setName("Eric Meyer"));
        assertSame(c1, c1.setName("Eric","Meyer"));
        assertSame(c1, c1.addContact("eric@gmail.com"));
    }

    /*
     * Regular test case 102: Default Constructor with setId(id) only
     * allowed to set id once.
     */
    @Test @Order(102)
    void test102_DefaultConstructorSetIdOnlyOnce() {
        final Customer c1 = new Customer();
        assertEquals(null, c1.getId());     // id is null (unassigned)
        c1.setId(648L);                     // set id for the first time
        assertEquals(648L, c1.getId());     // id is 648
        c1.setId(912L);                     // set id for the second time
        assertEquals(648L, c1.getId());     // id is still 648
    }
}
```

Create test methods for more **regular test cases** constructing
objects using the `Customer(String name)` constructor for:

- test case 110: `Customer c1 = new Customer("Eric Meyer");` -

- test case 111: `Customer c1 = new Customer("Meyer, Eric");` -
    the exptected *firstName* is: `"Eric"` and *lastName*: `"Meyer"`.

- test case 112: `Customer c1 = new Customer("Meyer");` -
    the exptected *firstName* is: `""` (empty String) and *lastName*: `"Meyer"`.

with corresponding test-methods:

```java
    /*
     * Regular test case 110: Constructor with regular first name last name.
     * new Customer("Eric Meyer"),  expected: firstName: "Eric", lastName: "Meyer"
     */
    @Test @Order(110)
    void test110_ConstructorWithRegularFirstLastName() {
        ...
    }

    /*
     * Regular test case 111: Constructor with regular last name comma first name.
     * new Customer("Meyer, Eric"),  expected: firstName: "Eric", lastName: "Meyer"
     */
    @Test @Order(111)
    void test111_ConstructorWithRegularLastCommaFirstName() {
        ...
    }

    /*
     * Regular test case 112: Constructor with regular single last name.
     * new Customer("Meyer"),  expected: firstName: "" (empty), lastName: "Meyer"
     */
    @Test @Order(112)
    void test112_ConstructorWithRegularLastNameOnly() {
        ...
    }
```

Create test methods for **corner test cases** constructing
objects with shortest or long(est) name arguments:

- test case 120: `Customer c1 = new Customer("E M");` -
    the exptected *firstName* is: `"E"` (empty String) and *lastName*: `"M"`.

- test case 121: `Customer c1 = new Customer("Nadine Ulla Maxine Adriane Blumenfeld");` -
    the exptected *firstName* is: `"Nadine Ulla Maxine Adriane"` and *lastName*: `"Blumenfeld"`.

- test case 122: `Customer c1 = new Customer("Nadine Ulla Maxine Adriane von-Blumenfeld-Bozo");` -
    the exptected *firstName* is: `"Nadine Ulla Maxine Adriane"` and *lastName*: `"von-Blumenfeld-Bozo"`.

- test case 123: `Customer c1 = new Customer("von-Blumenfeld-Bozo, Nadine Ulla Maxine Adriane");` -
    the exptected *firstName* is: `"Nadine Ulla Maxine Adriane"` and *lastName*: `"von-Blumenfeld-Bozo"`.

with corresponding test-methods:

```java
    /*
     * Corner test case 120: Constructor with shortest allowed first and last name.
     * test three cases:
     *  - new Customer("E M"),  expected: firstName: "E", lastName: "M"
     *  - new Customer("M, E"), expected: firstName: "E", lastName: "M"
     *  - new Customer("M"),    expected: firstName: "", lastName: "M"
     */
    @Test @Order(120)
    void test120_ConstructorWithCornerShortestPossibleFirstAndLastName() {
        ...
    }

    /*
     * Corner test case 121: Constructor with long first and last name.
     * new Customer("Nadine Ulla Maxine Adriane Blumenfeld")
     *  - expected: firstName: "Nadine Ulla Maxine Adriane", lastName: "Blumenfeld"
     */
    @Test @Order(121)
    void test121_ConstructorWithLongFirstAndLastName() {
        ...
    }

    /*
     * Corner test case 122: Constructor with long first and multi-part last name.
     * new Customer("Nadine Ulla Maxine Adriane von-Blumenfeld-Bozo")
     *  - expected: firstName: "Nadine Ulla Maxine Adriane", lastName: "von-Blumenfeld-Bozo"
     */
    @Test @Order(122)
    void test122_ConstructorWithLongFirstAndMultipartLastName() {
        ...
    }

    /*
     * Corner test case 123: Constructor with long first and multi-part last name.
     * new Customer("von-Blumenfeld-Bozo, Nadine Ulla Maxine Adriane")
     *  - expected: firstName: "Nadine Ulla Maxine Adriane", lastName: "von-Blumenfeld-Bozo"
     */
    @Test @Order(123)
    void test123_ConstructorWithLongMultipartLastNameAndFirstName() {
        ...
    }
```

Create test methods for **exception test cases** constructing
objects with empty or `null` name arguments:

- test case 130: `Customer c1 = new Customer("");` -
    the exptected outcome is that an `IllegalArgumentException` is thrown
    by the constructor with message: `"name empty"`.


- test case 131: `Customer c1 = new Customer(null);` -
    the exptected outcome is that an `IllegalArgumentException` is thrown
    by the constructor with message: `"name null"`.

```java
    /*
     * Exception test case 130: Constructor with empty name: "".
     * The exptected outcome is that an {@link IllegalArgumentException}
     * is thrown by the constructor with message: "name empty".
     */
    @Test @Order(130)
    void test130_ConstructorWithEmptyName() {
        ...
    }

    /*
     * Exception test case 131: Constructor with null argument.
     * The exptected outcome is that an {@link IllegalArgumentException}
     * is thrown by the constructor with message: "name null".
     */
    @Test @Order(131)
    void test131_ConstructorWithNullArgument() {
        ...
    }
```


&nbsp;

## 2. Create *Id* (200) Tests

*Id* (200) Tests test methods of class *Customer*:
- `getId()`
- `setId(long id)`

Develop a test plan and design test-methods (like above) covering the
following cases:

1. **Regular cases**: regulare cases stage regular use cases:
    - 200: test the value returned for `getId()` after object construction is: `null`.
    - 201: test value returned for `getId()` after first `setId(x)` is the set value: `x`.
    - 202: test value returned for `getId()` after second invocation of `setId(y)`
        is still the first value: `x`.

1. **Corner cases**:
    - 210: test `setId(x)` with minimum allowed value `x` and value `x+1`.
    - 211: test `setId(x)` with maximum allowed value `x` and value `x-1`.
    - 212: test `setId(0)` with value zero.

1. **Exception cases**:

    - 220: test `setId(-1)` illegal (exception) case that expects the method
        to throw an `IllegalArgumentException` with message: `"invalid id (negative)"`.
        Test both, that the exception is thrown and the exception message.

    - 221: test `setId(Long.MIN_VALUE)` illegal (exception) case that expects the method
        to throw an `IllegalArgumentException` with message: `"invalid id (negative)"`.
        Test both, that the exception is thrown and the exception message.

Create a new test class in package `datamodel` in `tests` with a
test method for the default constructor:

```java
/**
 * Tests for {@link Customer} class: [200..299] Id-tests with tested
 * methods:
 * <pre>
 * - getId()
 * - setId(long id)
 * </pre>
 * @author sgra64
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Customer_200_Id_Tests {
    ...
}
```


&nbsp;

## 3. Run *Name* (300) Tests

*Name* (300) Tests test methods of class *Customer*:
- `getFirstName()`
- `getLastName()`
- `setName(String name)`
- `setName(String first, String last)`

Install [Customer_300_Name_Tests.java](Customer_300_Name_Tests.java)
in package `datamodel` in `tests` and run tests.

Make sure tests run and fix tested code (your implementation of class *Customer*).


&nbsp;

## 4. Run *Contacts* (400) Tests

*Contacts* (400) Tests test methods of class *Customer*:
- `contactsCount()`
- `getContacts()`
- `addContact(String contact)`
- `deleteContact(int i)`
- `deleteAllContacts()`

Install [Customer_400_Contacts_Tests.java](Customer_400_Contacts_Tests.java)
in package `datamodel` in `tests` and run tests.

Make sure tests run and fix tested code (your implementation of class *Customer*).


&nbsp;

## 5. Run Extended *Name* (500) Tests

Extended *Name* (500) Tests test name-related methods of class *Customer*
with some extreme names.

Install [Customer_500_NameXXL_Tests.java](Customer_500_NameXXL_Tests.java)
in package `datamodel` in `tests` and run tests.

Make sure tests run and fix tested code (your implementation of class *Customer*).


&nbsp;

## 6. Run all *Customer* Tests

Run all Customer tests in the IDE and in the terminal:

```sh
mk compile compile-tests            # compile tested code and test code
mk run-tests
```

You can also run tests selectively (ommit failing tests):

```sh
java $(eval echo $JUNIT_CLASSPATH) org.junit.platform.console.ConsoleLauncher\
  $(eval echo $JUNIT_OPTIONS) \
  -c datamodel.Customer_100_Constructor_Tests
```

Output for all `Customer_100_Constructor_Tests` passing:

```
╷
├─ JUnit Jupiter ✔
│  └─ Customer_100_Constructor_Tests ✔
│     ├─ test100_DefaultConstructor() ✔
│     ├─ test101_DefaultConstructorChainableSetters() ✔
│     ├─ test102_DefaultConstructorSetIdOnlyOnce() ✔
│     ├─ test110_ConstructorWithRegularFirstLastName() ✔
│     ├─ test111_ConstructorWithRegularLastCommaFirstName() ✔
│     ├─ test112_ConstructorWithRegularLastNameOnly() ✔
│     ├─ test120_ConstructorWithCornerShortestPossibleFirstAndLastName() ✔
│     ├─ test121_ConstructorWithLongFirstAndLastName() ✔
│     ├─ test122_ConstructorWithLongFirstAndMultipartLastName() ✔
│     ├─ test123_ConstructorWithLongMultipartLastNameAndFirstName() ✔
│     ├─ test130_ConstructorWithEmptyName() ✔
│     └─ test131_ConstructorWithNullArgument() ✔
└─
Test run finished after 201 ms
[        12 tests successful      ]
[         0 tests failed          ]
```

Run `Customer_200_Id_Tests` tests:

```sh
java $(eval echo $JUNIT_CLASSPATH) org.junit.platform.console.ConsoleLauncher\
  $(eval echo $JUNIT_OPTIONS) \
  -c datamodel.Customer_200_Id_Tests
```

Output for all `Customer_200_Id_Tests` passing:

```
╷
├─ JUnit Jupiter ✔
│  └─ Customer_200_Id_Tests ✔
│     ├─ test200_IdNullAfterCconstruction() ✔
│     ├─ test201_setIdMinValue() ✔
│     ├─ test201_setIdRegularValue() ✔
│     ├─ test202_setIdRegularValueTwice() ✔
│     ├─ test210_setIdMinValue() ✔
│     ├─ test211_setIdMaxValue() ✔
│     ├─ test212_setIdZeroValue() ✔
│     └─ test220_setIdWithNegativeArguments() ✔
└─
Test run finished after 201 ms
[         8 tests successful      ]
[         0 tests failed          ]
```

Run all tests, if all tests are passing:

```sh
mk run-tests                        # run all tests

# or run tests selectively:
# 
java $(eval echo $JUNIT_CLASSPATH) org.junit.platform.console.ConsoleLauncher\
  $(eval echo $JUNIT_OPTIONS) \
  -c application.Application_0_always_pass_Tests \
  -c datamodel.Customer_100_Constructor_Tests \
  -c datamodel.Customer_200_Id_Tests \
  -c datamodel.Customer_300_Name_Tests \
  -c datamodel.Customer_400_Contacts_Tests \
  -c datamodel.Customer_500_NameXXL_Tests
```

Output for all tests passing for class *Customer*:

```
╷
├─ JUnit Jupiter ✔
│  ├─ Customer_400_Contacts_Tests ✔
│  │  ├─ test400_addContactsRegularCases() ✔
│  │  ├─ test401_addContactsCornerCases() ✔
│  │  ├─ test402_addContactsCornerCases() ✔
│  │  ├─ test403_addContactsMinimumLength() ✔
│  │  ├─ test404_addContactsIgnoreDuplicates() ✔
│  │  ├─ test410_deleteContactRegularCases() ✔
│  │  ├─ test411_deleteContactOutOfBoundsCases() ✔
│  │  └─ test412_deleteAllContacts() ✔
│  ├─ Application_0_always_pass_Tests ✔
│  │  ├─ test_001_always_pass() ✔
│  │  └─ test_002_always_pass() ✔
│  ├─ Customer_200_Id_Tests ✔
│  │  ├─ test200_IdNullAfterCconstruction() ✔
│  │  ├─ test201_setIdMinValue() ✔
│  │  ├─ test201_setIdRegularValue() ✔
│  │  ├─ test202_setIdRegularValueTwice() ✔
│  │  ├─ test210_setIdMinValue() ✔
│  │  ├─ test211_setIdMaxValue() ✔
│  │  ├─ test212_setIdZeroValue() ✔
│  │  └─ test220_setIdWithNegativeArguments() ✔
│  ├─ Customer_500_NameXXL_Tests ✔
│  │  ├─ test500_setNameMultipartLastName() ✔
│  │  ├─ test501_setNameMultipartLastName() ✔
│  │  ├─ test502_setNameMultipartLastName() ✔
│  │  ├─ test510_setNameDoubleFirstName() ✔
│  │  ├─ test511_setNameDoubleFirstName() ✔
│  │  ├─ test512_setNameDoubleFirstName() ✔
│  │  ├─ test520_setNameMultipartFirstNames() ✔
│  │  ├─ test521_setNameMultipartFirstNames() ✔
│  │  ├─ test522_setNameMultipartFirstNames() ✔
│  │  ├─ test530_setNameMultipartFirstNames() ✔
│  │  ├─ test531_setNameMultipartNames() ✔
│  │  ├─ test544_setNameMultiDashMultipartFirstNames() ✔
│  │  ├─ test550_setNameMultiDashMultipartFirstNames() ✔
│  │  ├─ test550_setNameExtremeLongNames() ✔
│  │  ├─ test551_setNameMultiDashMultipartFirstNames() ✔
│  │  ├─ test552_setNameMultipartNames() ✔
│  │  └─ test553_setNameMultiDashMultipartFirstNames() ✔
│  ├─ Customer_300_Name_Tests ✔
│  │  ├─ test300_setNameFirstAndLastName() ✔
│  │  ├─ test301_setNameFirstAndLastName() ✔
│  │  ├─ test302_setNameFirstAndLastName() ✔
│  │  ├─ test303_setNameFirstAndLastName() ✔
│  │  ├─ test310_setNameSingleName() ✔
│  │  ├─ test311_setNameSingleName() ✔
│  │  ├─ test312_setNameSingleName() ✔
│  │  ├─ test313_setNameSingleName() ✔
│  │  ├─ test320_setNameDoubleLastName() ✔
│  │  ├─ test321_setNameDoubleLastName() ✔
│  │  ├─ test322_setNameDoubleLastName() ✔
│  │  ├─ test330_setNameSingleArgumentConstructor() ✔
│  │  ├─ test331_setNameSingleArgumentConstructor() ✔
│  │  ├─ test332_setNameSingleArgumentConstructor() ✔
│  │  ├─ test333_setNameSingleArgumentConstructor() ✔
│  │  ├─ test334_setNameSingleArgumentConstructor() ✔
│  │  └─ test335_setNameSingleArgumentConstructor() ✔
│  └─ Customer_100_Constructor_Tests ✔
│     ├─ test100_DefaultConstructor() ✔
│     ├─ test101_DefaultConstructorChainableSetters() ✔
│     ├─ test102_DefaultConstructorSetIdOnlyOnce() ✔
│     ├─ test110_ConstructorWithRegularFirstLastName() ✔
│     ├─ test111_ConstructorWithRegularLastCommaFirstName() ✔
│     ├─ test112_ConstructorWithRegularLastNameOnly() ✔
│     ├─ test120_ConstructorWithCornerShortestPossibleFirstAndLastName() ✔
│     ├─ test121_ConstructorWithLongFirstAndLastName() ✔
│     ├─ test122_ConstructorWithLongFirstAndMultipartLastName() ✔
│     ├─ test123_ConstructorWithLongMultipartLastNameAndFirstName() ✔
│     ├─ test130_ConstructorWithEmptyName() ✔
│     └─ test131_ConstructorWithNullArgument() ✔
├─ JUnit Vintage ✔
└─ JUnit Platform Suite ✔

Test run finished after 856 ms
[         9 containers found      ]
[         0 containers skipped    ]
[         9 containers started    ]
[         0 containers aborted    ]
[         9 containers successful ]
[         0 containers failed     ]
[        64 tests found           ]
[         0 tests skipped         ]
[        64 tests started         ]
[         0 tests aborted         ]
[        64 tests successful      ]
[         0 tests failed          ]
done.
```

<!-- 
<img src="https: //raw.githubusercontent.com/sgra64/se1-bestellsystem/refs/heads/markup/c2-customer/Customer.png" alt="drawing" width="600"/>
 -->
