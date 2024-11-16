# Project: *se1-bestellsystem*

Project of a simple order processing system for the *Software Engineering-I*
course.

Class *Customer* has attributes:

- *id* (long) to identify the entity,

- *lastName* and *firstName* (String) for the Customer name and

- contacts (List<String>) as List of contacts, e.g. *email*, *phone number*.

Classes *Order* and *Article* also have *id*‐attributes.

<img src="https://raw.githubusercontent.com/sgra64/se1-bestellsystem/refs/heads/markup/main/concept-diagram.png" alt="drawing" width="600"/>

**Cardinalities (dimensions)** describe whether *one* (strict: `1` or
optional: `0..1`) or *many* (zero or more: `*` or at-least one or more: `1..*`)
objects of one side of a relation may exist. Is nothing is specified, `1`
is assumed.

One *Customer* may have many *Orders* - the cardinality of this relation
is: `[ 1 : * ]` (one with *Customer* and `*` with *Order*). In reverse,
it reads: each *Order* is assigned exactly one *Customer*.

The relation between *Order* and *OrderItem* is: `[ 1 : 1..* ]`.
It reads: each *Order* has at least one (one or more, but at least one)
*OrderItem*. In reverse, each *OrderItem* is assigned to exactly one
*Order*.

The relation between *OrderItem* and *Article* is: `[ * : 1 ]`.
It reads: each *OrderItem* refers to exactly one *Article*. In reverse,
*Articles* may be referred to in many *OrderItems*.

**Relationships** between classes describe how classes (more precisely: their objects)
relate to one other.

- **Aggregation (white Diamond)** expresses a logical association (*"ownership"*)
    of one class to another. *Orders* are always assigned to *Customers*.
    *Orders* are not included in *Customer* objects. An *Order* cannot exist without
    a *Customer*.

    The *Aggregation* relationship is implemented by a reference in the "*owned*"
    class to the *"owning"* class. Class *Customer* has no information of the *Orders*
    the *Customer* owns. But each *Order* has a reference to the owning *Customer*.

- **Composition (black Diamond)** expresses a *"part-of"* relation. Elements of
    one class are part of another. Class *Order* contains a list of *OrderItems*.

    Since class *Order* contains its *OrderItems*, this information is not included
    in *OrderItem*. Just considering an *OrderItem*, the *Order* it belongs to cannot
    be determined. Inclusion also implies that *OrderItems* cannot exist without the
    containing *Order*.

- **Association** (a line without diamond) expresses any other relation between
    classes that is not *"ownership"* or *"part_of"* such as between *OrderItem*
    and *Article*. An *Article* exists without dependency of *OrderItems* referring
    to it.


&nbsp;

# Project Setup: *se1-bestellsystem*

The setup-process of the order processing system for the *Software Engineering-I*
course has steps:

1. [Project Assembly](#1-project-assembly)
1. [Change Project to *"SE-1 Bestellsystem"*](#2-change-project-to-se-1-bestellsystem)
1. [Project Build](#3-project-build)
1. [Branch Clean Up](#4-branch-clean-up)
1. [Check Project into Own Repository](#5-check-project-into-own-repository)
1. [*Setup* and *Build* Automation](#6-setup-and-build-automation)


&nbsp;

## 1. Project Assembly

The project is assembled from two branches:

- branch: [*main*](https://github.com/sgra64/se1-play/tree/main) and

- branch: [*libs*](https://github.com/sgra64/se1-play/tree/libs)
    of the [*se1-play*](https://github.com/sgra64/se1-play)
    repository and one

- [*patch*](https://github.com/sgra64/se1-bestellsystem/tree/se1-play-patch)
    applied from the
    [*se1-bestellsystem*](https://github.com/sgra64/se1-bestellsystem)
    repository to change (*"patch"*) the project identity from *"se1-play"*
    to *"se1-bestellsystem"*.

Create a new project directory: *se1-bestellsystem* in your workspace
(directory where other projects are) and pull both branches.

```sh
mkdir se1-bestellsystem             # create new project directory
cd se1-bestellsystem                # cd into project directory

git init                            # initialize new local git repository for the project

# set link named 'se1-play' to remote repository
git remote add se1-play https://github.com/sgra64/se1-play.git

# pull the 'main' branch from the 'se1-play' repository
git pull se1-play main

# clone the 'libs' branch to the 'branches' directory
mkdir branches
cd branches
git clone -b libs --single-branch https://github.com/sgra64/se1-play.git libs
cd ..

# source the project
source .env.sh

# verify the content of the project directory
ls -la
```
```
drwxr-xr-x 1     0 Nov  5 19:33 ./
drwxr-xr-x 1     0 Nov  5 18:55 ../
-rw-r--r-- 1  3518 Nov  5 19:33 .classpath
-rw-r--r-- 1 26135 Nov  5 19:28 .env.sh
drwxr-xr-x 1     0 Nov  5 19:28 .git/
-rw-r--r-- 1  1214 Nov  5 19:28 .gitignore
-rw-r--r-- 1   432 Nov  5 19:33 .project
drwxr-xr-x 1     0 Nov  5 19:33 .vscode/
-rw-r--r-- 1 25892 Nov  5 19:32 README.md
drwxr-xr-x 1     0 Nov  5 19:33 bin/
drwxr-xr-x 1     0 Nov  5 19:30 branches/
lrwxrwxrwx 1    15 Nov  5 19:33 libs -> ./branches/libs/
drwxr-xr-x 1     0 Nov  5 19:28 resources/
drwxr-xr-x 1     0 Nov  5 19:28 src/
drwxr-xr-x 1     0 Nov  5 19:28 tests/
```

Test-build the project:

```sh
mk compile compile-tests run-tests run A BB CCC
```
```
java application.Runtime A BB CCC
Hello, "SE-1 Play" (application.Application)
- arg: A
- arg: BB
- arg: CCC
```


&nbsp;

## 2. Change Project to *"SE-1 Bestellsystem"*

The name *"SE-1 Play"* still appears. Changes need to be applied to fit the
project to the new name *"SE-1 Bestellsystem"*.

```sh
# set link named 'se1-patch-repository' point to repository to fetch patch
git remote add se1-patch-repository https://github.com/sgra64/se1-bestellsystem.git

# fetch branch 'se1-patch' from the remote repository
git fetch se1-patch-repository se1-patch
```
```
remote: Enumerating objects: 32, done.
remote: Counting objects: 100% (32/32), done.
remote: Compressing objects: 100% (26/26), done.
remote: Total 32 (delta 3), reused 32 (delta 3), pack-reused 0 (from 0)Unpacking
Unpacking objects: 100% (32/32), 27.88 KiB | 385.00 KiB/s, done.

From https://github.com/sgra64/se1-bestellsystem
 * branch            se1-patch  -> FETCH_HEAD
 * [new branch]      se1-patch  -> se1-patch-repository/se1-patch
```

A new *remote*-branch: `se1-patch-repository/se1-patch` was created in the
local repository with the content of the remote branch.

Content of the branch can be compared to the local `main` branch:

```sh
# compare content of fetched branch to local 'main' branch:
git diff --name-status se1-patch-repository/se1-patch main
```

Files labeled with `M` represent modifications:

```
A       .env.sh
A       .vscode/launch.json
A       .vscode/launch_terminal.sh
A       .vscode/settings.json
M       README.md                               <-- modified
A       resources/META-INF/MANIFEST.MF
M       resources/application.properties        <-- modified
A       resources/log4j2.properties
A       src/application/Application.java
A       src/application/Runtime.java
M       src/application/package-info.java       <-- modified
M       src/module-info.java                    <-- modified
A       tests/application/Application_0_always_pass_Tests.java
```

Modifications can be displayed, e.g. for file [src/module-info.java](src/module-info.java):

```sh
git diff main se1-patch-repository/se1-patch -- src/module-info.java
```

Red lines labeled with `-` show current content of the file in the `main` branch,
green lines labeled with `+` show the content of the file from the fetched branch.

Changes in file [src/module-info.java](src/module-info.java) relate to renaming
the module from *"se1.play"* to *"se1.bestellsystem"*.

```
- * Module {@code se1.play} demonstrates Java project setup, sourcing the project
- * and the project build process for the <i>Software Engineering-I</i> course.
+ * Module {@code se1.bestellsystem} implements a simple order processing system
+ * for the <i>Software Engineering-I</i> course.

...

-module se1.play {                  <-- current line (old module name)
+module se1.bestellsystem {         <-- incoming line (correct new module name)
     opens application;     // open: package is accessible by JavaVM at runtime
     exports application;   // export: package is accessible to compile other mo
dules
```

See changes in all modified files:

```sh
git diff main se1-patch-repository/se1-patch -- src/module-info.java

git diff main se1-patch-repository/se1-patch -- src/application/package-info.java

git diff main se1-patch-repository/se1-patch -- resources/application.properties
```

Changes fetched from the remote branch *se1-patch-repository/se1-patch*
cam be applied by a `git merge`. In a merge, git integrates (merges) changes
from a *branch-to-merge* into the *current branch*.

```sh
# merge fetched branch into 'main' branch
git merge se1-patch-repository/se1-patch --allow-unrelated-histories
```

This merge leads to conflicts, which means that *git* was not able to integrate
text in files from the *current* (main) branch and from the *incomming* branch
(se1-patch):

```
Auto-merging README.md
CONFLICT (add/add): Merge conflict in README.md
Auto-merging resources/application.properties
CONFLICT (add/add): Merge conflict in resources/application.properties
Auto-merging src/application/package-info.java
CONFLICT (add/add): Merge conflict in src/application/package-info.java
Auto-merging src/module-info.java
CONFLICT (add/add): Merge conflict in src/module-info.java
Automatic merge failed; fix conflicts and then commit the result.
```

Open file: `src/module-info.java` to see the insertione git has made to
indicate conflicts:

```git
<<<<<<< HEAD
module se1.play {
=======
module se1.bestellsystem {
>>>>>>> se1-patch-repository/se1-patch
```

Lines between markers: `<<< HEAD` and `===` show the content of the file
from the `main` branch (*"current"*). Lines between markers `===` and `>>>`
show content of the file from the (*"incoming"*) branch.

Since *git* has inserted markers as text into files, the Java compiler will
report errors:

```sh
mk compile                          # fails with open merge conflicts
```
```
src\application\package-info.java:16: error: illegal start of type
<<<<<<< HEAD
^
src\application\package-info.java:16: error: > expected
<<<<<<< HEAD
            ^
```

For *git*, the project is in an state with: *unmerged paths*, which means
the merge has not been completed - or: "the merge is still open."

```sh
git status                          # show status of the 'open merge'
```
```
On branch main
You have unmerged paths.            <-- "unmerged paths" (open merge)
  (fix conflicts and run "git commit")
  (use "git merge --abort" to abort the merge)

Unmerged paths:                     <-- list of files with open merge conflicts
  (use "git add <file>..." to mark resolution)
        both added:      README.md
        both added:      resources/application.properties
        both added:      src/application/package-info.java
        both added:      src/module-info.java
```

Merge conflicts must be resolved manually by opening files one after another
and inserting the correct text.

An *"open merge"* can always be reset restoring the status of the project
before the merge:

```sh
git merge --abort                   # abort merge and restore previous project state
git status                          # no more 'unmerged paths'

mk compile run A BB CCC             # the project compiles and runs again
```

```
java application.Runtime A BB CCC
Hello, "SE-1 Play" (application.Application)
- arg: A
- arg: BB
- arg: CCC
```

Name *"SE-1 Play"* still appears.

Repeat the merge and resolve all conflicts. IDE provide support, usually by offering
selections: *Accept Current* or *Accept Incoming* changes.

Insert your name as `Author` in file
[src/application/package-info.java](src/application/package-info.java).

```sh
# merge fetched branch into 'main' branch (squash commits from merged branch)
git merge --squash se1-patch-repository/se1-patch --allow-unrelated-histories
```

Resolve all conflicts using your IDE.

Make sure the project compiles and runs at the end.

```sh
mk compile run A BB CCC             # the project compiles and runs again
```

If the project compiles and runs, the still *open merge* can be committed:

```sh
git status                          # show files with modifications (red)

# prepare ("stage") commit
git add \
    README.md \
    resources/application.properties \
    src/application/package-info.java \
    src/module-info.java

git status                          # show staged files (green)

# commit ("record") staged files in the local repository
git commit -m "merge commit 'se1-patch'"

git log --oneline                   # show new merge-commit on top of 'main'-branch ('HEAD')
```

The *git*-log shows the new merge-commit on top of 'main'-branch ('HEAD').
It also shows the commit history inherited with the merges.

```
79d62fd (HEAD -> main) Merge commit 'se1-patch'     <-- new merge commit
\\
05d74c0 (se1-patch-repository/se1-patch) add patch files, update README.md
187123e se1-play main branch
30c6a74 (se1-play/main) update .env.sh, in wipe keep libs link
b2dceeb update Runtime.java with new bean implementation
60269fb add folders: src tests resources, update README.md
e04da83 add .env.sh, update README.md
8e2a84c add .vscode settings, update README.md
3a20b05 initial commit: .gitignore, README.md
```


&nbsp;

## 3. Project Build

Rebuild the project:

```sh
mk build                # compile compile-tests run-tests package

java -jar bin/application-1.0.0-SNAPSHOT.jar A BB CCC DDDD
```

The correct name of the project: *"SE-1 Bestellsystem"* defined in:
[application.properties](resources/application.properties)
appears.

```
Hello, "SE-1 Bestellsystem" (application.Application)
- arg: A
- arg: BB
- arg: CCC
- arg: DDDD
```

```sh
mk javadoc              # create javadoc
```

Open documentation: `docs/index.html` and show that your name
appears on pages as `Author: your name`. The name is defined in
[application.package-info.java](src/application/package-info.java).


&nbsp;

## 4. Branch Clean Up

Remove remote branches from project:

```sh
git branch -avv                     # show all branches
```
```
* main                      31292ba merge commit remote branch se1-patch-repository/se1-patch
  remotes/se1-play/main     30c6a74 update .env.sh, in wipe keep libs link
  remotes/se1-patch-repository/se1-patch f06822b update module-info.java package-info.java...
```

Remove remote branches (`-dr`: delete, remote):

```sh
git branch -dr se1-play/main
git branch -dr se1-patch-repository/se1-patch
git branch -avv                     # show all branches
```

Remote branches are gone. Only the *main*-branch remains in the local repository.

```
* main                      31292ba merge commit remote branch se1-patch-repository/se1-patch
```

Next, links to remote repositories used for project assembly are removed:

```sh
git remote -v                       # show remote repository links
```
```
se1-patch-repository    https://github.com/sgra64/se1-bestellsystem.git (fetch)
se1-patch-repository    https://github.com/sgra64/se1-bestellsystem.git (push)
se1-play        https://github.com/sgra64/se1-play.git (fetch)
se1-play        https://github.com/sgra64/se1-play.git (push)
```

Links are also included in file: `.git/config`:

```sh
cat .git/config                     # show local git 'config' file
```
```
[core]
        repositoryformatversion = 0
        filemode = false
        bare = false
        logallrefupdates = true
        ignorecase = true
[remote "se1-play"]
        url = https://github.com/sgra64/se1-play.git
        fetch = +refs/heads/*:refs/remotes/se1-play/*
[remote "se1-patch-repository"]
        url = https://github.com/sgra64/se1-bestellsystem.git
        fetch = +refs/heads/*:refs/remotes/se1-patch-repository/*
```

Link `se1-play` is removed. Link `se1-patch-repository` will be kept
for future patches.

```sh
git remote remove se1-play          # remove remote repository link 'se1-play'

git remote -v                       # show remote repository links
cat .git/config                     # show local git 'config' file
```

Link `se1-play` is removed.

Finally, the commit history inherited from merges is cleaned up.

```sh
git log --oneline
```
```
79d62fd (HEAD -> main) Merge commit 'se1-patch'
05d74c0 add patch files, update README.md
187123e se1-play main branch
30c6a74 update .env.sh, in wipe keep libs link
b2dceeb update Runtime.java with new bean implementation
60269fb add folders: src tests resources, update README.md
e04da83 add .env.sh, update README.md
8e2a84c add .vscode settings, update README.md
3a20b05 initial commit: .gitignore, README.md
```

All commits will be collapsed into one new commit on the *main*-branch:

```sh
# collapse commits inherited from merges into one
git reset $(git commit-tree HEAD^{tree} -m "se1-bestellsystem base commit")

git log --oneline
```

The new commit log shows one new commit that includes the current
project state (a new commit was created with new commit-id):

```
779b7b7 (HEAD -> main) se1-bestellsystem base commit
```


&nbsp;

## 5. Check Project into Own Repository

Create a new project with name: *"se1-bestellsystem"* in your
[BHT GitLab](https://gitlab.bht-berlin.de/)
(or other repository).

Make sure, your `public ssh key` is registered in your account.

Obtain the *ssh*-repository URL, e.g.
`git@gitlab.bht-berlin.de:<your-id>/se1.bestellsystem.git`

and register in the local repository under the name `origin` as new
remote repository:

```sh
# register the remote repository URL under the name 'origin'
# make sure to replace the '<...>' with your account id
git remote add origin git@gitlab.bht-berlin.de:<...>/se1.bestellsystem.git

# show the new remote URL
git remote -v
```

The new URL is registered under the name `origin`. The prior remote URL
is still registered under the name `se1-play`.

```
origin  git@gitlab.bht-berlin.de/se1-bestellsystem.git (fetch)
origin  git@gitlab.bht-berlin.de/se1-bestellsystem.git (push)
se1-patch-repository    https://github.com/sgra64/se1-bestellsystem.git (fetch)
se1-patch-repository    https://github.com/sgra64/se1-bestellsystem.git (push)
```

Show remote URL in file: [.git/config](.git/config)

```
[core]
        repositoryformatversion = 0
        filemode = false
        bare = false
        logallrefupdates = true
        ignorecase = true
[remote "origin"]
        url = git@gitlab.bht-berlin.de/se1-bestellsystem.git
        fetch = +refs/heads/*:refs/remotes/origin/*
[remote "se1-patch-repository"]
        url = https://github.com/sgra64/se1-bestellsystem.git
        fetch = +refs/heads/*:refs/remotes/se1-patch-repository/*
```

Finally, the main branch can be pushed to remote `origin`:

```sh
git push --set-upstream origin main     # push branch 'main' to remote 'origin'

# or short version:
git push -u origin main                 # short for pushing branch
```


&nbsp;

## 6. *Setup* and *Build* Automation

The *setup* process can be automated by summarizing commands in a
`setup()` function. The function verifies it is executed in an
empty project directory named *"se1-bestellsystem"*:

```sh
# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
# setup() function automates initial 'se1-bestellsystem' assembly starting
# in an empty project directory 'se1-bestellsystem'.
# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
function setup() {
    local current_dir=$(basename $(pwd))
    [ ! "$current_dir" = "se1-bestellsystem" -o "$(ls -A)" ] && \
        echo "setup() must run in an empty directory named 'se1-bestellsystem'" && \
        return

    # create local git repository
    git init

    # set remote and pull 'main' branch from 'se1-play'
    git remote add se1-play https://github.com/sgra64/se1-play.git
    git pull se1-play main

    # pull 'libs' branch from 'se1-play' repository
    mkdir branches
    cd branches
    git clone -b libs --single-branch https://github.com/sgra64/se1-play.git libs
    cd ..

    # apply 'se1-patch'
    git remote add se1-patch-repository https://github.com/sgra64/se1-bestellsystem.git
    git fetch se1-patch-repository se1-patch

    tar cvf ours.tar README.md      # preserve README.md
    # 
    # merge patch with strategy to accept incoming ("theirs") changes
    git merge --squash se1-patch-repository/se1-patch \
        --no-commit \
        --allow-unrelated-histories \
        --strategy-option theirs
    # 
    tar xvf ours.tar                # restore preserved content
    # 
    git add .                       # stage and commit the open merge
    git commit -m "merge commit 'se1-patch'"

    # cleanup: remove fetched branches, remote links, collapse commit history
    rm ours.tar
    git branch -dr se1-play/main
    git branch -dr se1-patch-repository/se1-patch
    git remote remove se1-play
    git reset $(git commit-tree HEAD^{tree} -m "se1-bestellsystem base commit")

    source .env.sh                  # source the project
}
```

Functions can be defined in the terminal shell by copying their definitions
into the terminal.
The final automation can be executed:

```sh
# prepare automated project setup from scratch
mkdir se1-bestellsystem     # create new project directory
cd se1-bestellsystem        # cd into project directory

setup                       # run automated setup() from scratch

mk build                    # build project
```

```
\\
final product artefact built:
-rw-r--r-- 1 svgr2 Kein 14640 Nov 16 22:15 bin/application-1.0.0-SNAPSHOT.jar
done.
```

```sh
# run the final product artefact
java -jar bin/application-1.0.0-SNAPSHOT.jar A BB CCC
```

Remove `setup()` function definition:

```sh
unset -f setup
```
