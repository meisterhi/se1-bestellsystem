# C2: Class *Customer*

1. [Create Class *Customer*](#1-create-class-customer)
1. [Create Driver for Class *Customer*](#2-create-driver-for-class-customer)
1. [Create Javadoc for Class *Customer*](#3-create-javadoc-for-class-customer)
1. [Commit and Push Changes](#4-commit-and-push-changes)


&nbsp;

## 1. Create Class *Customer*

Create a new package: `datamodel` in the project *se1-bestellsystem*
and within this package a new class `Customer` from the UML definition:

<img src="https://raw.githubusercontent.com/sgra64/se1-bestellsystem/refs/heads/markup/c2-customer/Customer.png" alt="drawing" width="600"/>

The exact specification of methods can be found in the
[*Customer Javadoc*](https://sgra64.github.io/se1.bestellsystem/D12-Datamodel/javadoc/se1.bestellsystem/datamodel/Customer.html).

Read carefully about the behavior of methods, e.g. that method
[*setId(long id)*](https://sgra64.github.io/se1.bestellsystem/D12-Datamodel/javadoc/se1.bestellsystem/datamodel/Customer.html#setId(long)) will sets the `id` only once when first called and leaving `id` value unchanged for subsequent invocations.

Read about [*method chaining*](https://www.geeksforgeeks.org/method-chaining-in-java-with-examples/)
and see how it is used in class 
[*Customer*](https://sgra64.github.io/se1.bestellsystem/D12-Datamodel/javadoc/se1.bestellsystem/datamodel/Customer.html).

Modify [*src/module-info.java*](https://github.com/sgra64/se1-bestellsystem/blob/main/src/module-info.java)
to open and export the new package `datamodel`:

```java
module se1.bestellsystem {
    opens application;      // open: package is accessible by JavaVM at runtime
    exports application;    // export: package is accessible to compile other modules
    opens datamodel;        // open: package is accessible by JavaVM at runtime
    exports datamodel;      // export: package is accessible to compile other modules

    requires org.junit.jupiter.api;     // JUnit-5 module for JUnit testing
    requires transitive org.slf4j;      // slf4j/log4j2 logging modules
    requires org.apache.logging.log4j.core;
    requires org.apache.logging.log4j;
}
```


&nbsp;

## 2. Create Driver for Class *Customer*

Create a new class `Application_C2` in package `application`:

<!-- 
pull example from
https://gitlab.bht-berlin.de/sgraupner/se1.bestellsystem/-/blob/C34/src/application/Application_C3.java?ref_type=heads -->

```java
package application;

import application.Runtime.Bean;
import datamodel.Customer;

import java.util.*;


/**
 * Driver class for the <i>c2-customer</i> assignment. Class creates some
 * {@link Customer} objects and outputs some results.
 * Class implements the {@link Runtime.Runnable} interface.
 * 
 * @version <code style=color:green>{@value application.package_info#Version}</code>
 * @author <code style=color:blue>{@value application.package_info#Author}</code>
 */
@Bean(priority=2)
public class Application_C2 implements Runtime.Runnable {

    /**
     * None-public default constructor (avoid javadoc warning).
     */
    public Application_C2() { }


    /**
     * Method of the {@link Runtime.Runnable} interface called on an instance
     * of this class created by the {@link Runtime}. Program execution starts here.
     * @param properties properties from the {@code application.properties} file
     * @param args arguments passed from the command line
     */
    @Override
    public void run(Properties properties, String[] args) {

        final Customer eric = new Customer("Eric Meyer")
            .setId(892474L)     // set id, first time
            .setId(947L)        // ignored, since id can only be set once
            .addContact("eric98@yahoo.com")
            .addContact("eric98@yahoo.com") // ignore duplicate contact
            .addContact("(030) 3945-642298");

        final Customer anne = new Customer("Anne Bayer")
            .setId(643270L)
            .addContact("anne24@yahoo.de")
            .addContact("(030) 3481-23352")
            .addContact("fax: (030)23451356");

        final Customer tim = new Customer("Tim Schulz-Mueller")
            .setId(286516L)
            .addContact("tim2346@gmx.de");

        final Customer nadine = new Customer("Nadine-Ulla Blumenfeld")
            .setId(412396L)
            .addContact("+49 152-92454");

        final Customer khaled = new Customer()
            .setName("Khaled Saad Mohamed Abdelalim")
            .setId(456454L)
            .addContact("+49 1524-12948210");

        List<Customer> customers = List.of(eric, anne, tim, nadine, khaled);
        //
        // print customer list
        customers.stream()
            .map(c -> print(c))     // .map(this::print)
            .forEach(System.out::println);
    }

    /**
     * Print customer attributes to StringBuffer and return as String.
     * @param customer object to print
     * @return customer attributes as String
     */
    String print(Customer customer) {
        StringBuffer sb = new StringBuffer(" - customer id: ");
        sb.append(customer.getId())
            .append(", name: ")
            .append(customer.getLastName())
            .append(", ")
            .append(customer.getFirstName());
        return sb.toString();
    }

    /**
     * JavaVM entry method that calls the {@link Runtime}, which creates
     * an instance of this class and invokes the
     * {@code run(properties, args)} - method.
     * @param args arguments passed from command line
     */
    public static void main(String[] args) {
        Runtime.run(args);
    }
}
```

Run the code in your IDE and in the terminal:

```sh
mk run                          # run code
```

Make sure you implemented `setId()` correctly such that it can
only be set once (when first called, see:
[*setId(long id) Javadoc*](https://sgra64.github.io/se1.bestellsystem/D12-Datamodel/javadoc/se1.bestellsystem/datamodel/Customer.html#setId(long))).
Otherwise `id` values will not match the output below.

Output:

```
 - customer id: 892474, name: Meyer, Eric       <-- id: 892474, not 974 (second attempt: setid(974L); )
 - customer id: 643270, name: Bayer, Anne
 - customer id: 286516, name: Schulz-Mueller, Tim
 - customer id: 412396, name: Blumenfeld, Nadine-Ulla
 - customer id: 456454, name: Abdelalim, Khaled Saad Mohamed
```

Change the code such that it also outputs contacts.
Package and run the final product (.jar):

```sh
mk package                      # package code and run .jar
java -jar bin/application-1.0.0-SNAPSHOT.jar
```

Output:

```
 - customer id: 892474, name: Meyer, Eric, contacts: [eric98@yahoo.com, (030) 3945-642298]
 - customer id: 643270, name: Bayer, Anne, contacts: [anne24@yahoo.de, (030) 3481-23352, fax: (030)23451356]
 - customer id: 286516, name: Schulz-Mueller, Tim, contacts: [tim2346@gmx.de]
 - customer id: 412396, name: Blumenfeld, Nadine-Ulla, contacts: [+49 152-92454]
 - customer id: 456454, name: Abdelalim, Khaled Saad Mohamed, contacts: [+49 1524-12948210]
```

Change the code such that it outputs customers in alphabetical order:

Output in alphabetical order:

```
 - customer id: 456454, name: Abdelalim, Khaled Saad Mohamed, contacts: [+49 1524-12948210]
 - customer id: 643270, name: Bayer, Anne, contacts: [anne24@yahoo.de, (030) 3481-23352, fax: (030)23451356]
 - customer id: 412396, name: Blumenfeld, Nadine-Ulla, contacts: [+49 152-92454]
 - customer id: 892474, name: Meyer, Eric, contacts: [eric98@yahoo.com, (030) 3945-642298]
 - customer id: 286516, name: Schulz-Mueller, Tim, contacts: [tim2346@gmx.de]
```


&nbsp;

## 3. Create Javadoc for Class *Customer*

Javadoc for Class *Customer* needs to be written as
[Javadoc comments](https://de.wikipedia.org/wiki/Javadoc).

Documentation for the package is included in a file `package-info.java`
in package `datamodel`. Create this file with following content:

```java
/**
 * Package with data model classes for the {@link se1.bestellsystem}.
 * <p>
 * The package provides classes for entities and relations managed by
 * the order processing application.
 * </p>
 * 
 * @version <code style=color:green>{@value application.package_info#Version}</code>
 * @author <code style=color:blue>{@value application.package_info#Author}</code>
 */

package datamodel;
```

Make sure
[*src/module-info.java*](https://github.com/sgra64/se1-bestellsystem/blob/main/src/module-info.java)
has the new package `datamodel` opened and exported
(see [*Step-1*](#1-create-class-customer)).

The final Javadoc page should look like
[*Customer Javadoc*](https://sgra64.github.io/se1.bestellsystem/D12-Datamodel/javadoc/se1.bestellsystem/datamodel/Customer.html):

Make sure, your name appears as `Author` (if not, change in: *src/application/package-info.java*):

<img src="https://raw.githubusercontent.com/sgra64/se1-bestellsystem/refs/heads/markup/c2-customer/Customer_Javadoc.png" alt="drawing" width="600"/>


&nbsp;

## 4. Commit and Push Changes

Commit changes you made to the project and push to your remote repository:

```sh
git status                      # show changed files
git add .                       # stage changes for commit
git commit -m "c2: package 'datamodel' with class Customer.java"

git push                        # push the commit to your upstream remote repository
```

If you can't push, make sure you got
[*Step-5: Check Project into Repository*](https://github.com/sgra64/se1-bestellsystem)
right from the *se1-bestellsystem* project setup.
