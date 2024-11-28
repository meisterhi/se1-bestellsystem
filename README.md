# Project: *se1-bestellsystem*

Project of a simple order processing system for the *Software Engineering-I*
course.

Class *Customer* has attributes:

- *id* (long) to identify a *Customer*,

- *lastName* and *firstName* (String) for the *Customer* name and

- contacts as List of contacts (String), e.g. *email*, *phone number*.

Classes *Order* and *Article* have also an *id* and other attributes.

<img src="https://raw.githubusercontent.com/sgra64/se1-bestellsystem/refs/heads/markup/main/concept-diagram.png" alt="drawing" width="600"/>


**Cardinalities (dimensions)** describe whether *one* (strict: `1` or
optional: `0..1`) or *many* (zero or more: `*` or at-least one: `1..*`)
objects of one side of a relation may exist. If nothing is specified,
strict `1` is assumed.

One *Customer* may have many *Orders* - the cardinality of this relation
is: `[ 1 : * ]` (`1` with *Customer* and `*` with *Order*). In reverse
direction it reads: each *Order* is assigned exactly one *Customer*.

The relation between *Order* and *OrderItem* is: `[ 1 : 1..* ]`.
It reads: each *Order* has at least one (one or more, but at least one)
*OrderItem*. In reverse, each *OrderItem* is assigned to exactly one
*Order*.

The relation between *OrderItem* and *Article* is: `[ * : 1 ]`.
It reads: each *OrderItem* refers to exactly one *Article*. In reverse,
*Articles* may be referred to by many *OrderItems*.

**Relationships** between classes describe how classes (more precisely: their objects)
relate to one other.

- **Aggregation (white Diamond)** expresses a logical association (*"ownership"*)
    of one class to another. *Orders* are always assigned to *Customers*.
    *Orders* are not included in *Customer* objects. An *Order* cannot exist without
    the owning *Customer*.

    The *Aggregation* relationship is implemented by a reference in the "*owned*"
    class to the *"owning"* class. Class *Customer* has no information about the
    *Orders* the *Customer* owns. But each *Order* has a reference to their owning
    *Customer*.

- **Composition (black Diamond)** expresses a *"part-of"* relation. Elements of
    one class are part of another. Class *Order* contains a list of *OrderItems*.

    Since class *Order* contains *OrderItems*, this information is not included
    in *OrderItem*. Just considering an *OrderItem*, the *Order* it belongs to cannot
    be determined. Inclusion implies that *OrderItems* cannot exist without the
    containing *Order*. *OrderItem* objects are managed by the including class
    and may not need identifiers (no *id* attribute).

- **Association** (a line without diamond) expresses any other relation between
    classes that is not *"ownership"* or *"part_of"* such as between *OrderItem*
    and *Article*. An *Article* may exist without an *OrderItem*.


&nbsp;

# Project Setup: *se1-bestellsystem*

The setup-process of the order processing system for the *Software Engineering-I*
course has the following steps:

1. [Project Assembly](#1-project-assembly)
1. [Change Project Identity to *"SE-1 Bestellsystem"*](#2-change-project-identity-to-se-1-bestellsystem)
1. [Project Build](#3-project-build)
1. [Branch Clean Up](#4-branch-clean-up)
1. [Check Project into Own Repository](#5-check-project-into-own-repository)
1. [*Setup* and *Build* Automation](#6-setup-and-build-automation)


&nbsp;

## 1. Project Assembly

The project is assembled from two branches:

- branch: [*main*](https://github.com/sgra64/se1-play/tree/main) of the previous
    [*se1-play*](https://github.com/sgra64/se1-play) repository and

- branch: [*libs*](https://github.com/sgra64/se1-play/tree/libs)
    of the same repository.

- A [*patch*](https://github.com/sgra64/se1-bestellsystem/tree/se1-patch)
    is applied from the new
    [*se1-bestellsystem*](https://github.com/sgra64/se1-bestellsystem)
    repository to change (*fix*, *"patch"*) the project identity from
    *"se1-play"* to *"se1-bestellsystem"*.

Create a new project directory: *se1-bestellsystem* in your workspace
(the directory where you keep projects) and pull both branches.

```sh
mkdir se1-bestellsystem             # create new project directory
cd se1-bestellsystem                # cd into project directory

git init                            # initialize new local git repository for the project

# set link named 'se1-play' to the remote repository
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

## 2. Change Project Identity to *"SE-1 Bestellsystem"*

The name *"SE-1 Play"* still appears. Changes need to be applied to fit the
project with the new name: *"SE-1 Bestellsystem"*. Changes are *"patched"*
into the project from the second remote repository:

```sh
# set link named 'se1-patch-repository' to point to the patch repository
git remote add se1-patch-repository https://github.com/sgra64/se1-bestellsystem.git

# fetch branch 'se1-patch' from the remote repository
git fetch se1-patch-repository se1-patch
```
```
remote: Enumerating objects: 45, done.
remote: Counting objects: 100% (7/7), done.
remote: Compressing objects: 100% (6/6), done.
remote: Total 45 (delta 1), reused 5 (delta 0), pack-reused 38 (from 1)
Unpacking objects: 100% (45/45), 30.43 KiB | 183.00 KiB/s, done.
From https://github.com/sgra64/se1-bestellsystem
 * branch            se1-patch  -> FETCH_HEAD
 * [new branch]      se1-patch  -> se1-patch-repository/se1-patch
```

A new *remote*-branch: `se1-patch-repository/se1-patch` was created in the
local repository with the content of the remote branch.

The content of the new branch can be compared to the local `main` branch:

```sh
# compare content of fetched branch to local 'main' branch:
git diff --name-status main se1-patch-repository/se1-patch
```

Files labeled with `M` represent modifications:

```
M       .env.sh                                 <-- M: modified
M       .vscode/launch.json                     <-- M: modified
D       .vscode/launch_terminal.sh
D       .vscode/settings.json
M       README.md                               <-- M: modified
D       resources/META-INF/MANIFEST.MF
M       resources/application.properties        <-- M: modified
D       resources/log4j2.properties
A       setup.sh                                <-- A: added (new file that comes with patch)
D       src/application/Application.java
D       src/application/Runtime.java
M       src/application/package-info.java       <-- M: modified
M       src/module-info.java                    <-- M: modified
D       tests/application/Application_0_always_pass_Tests.java
```

Modifications can be displayed for specific files, e.g. for
[*src/module-info.java*](src/module-info.java):

```sh
# show differences of file 'src/module-info.java' between the current and the incoming branch
git diff main se1-patch-repository/se1-patch -- src/module-info.java
```

Red lines labeled with `-` show the content of the file in the `main` branch,
green lines labeled with `+` show the content of the file from the fetched branch.

The changes in file [*src/module-info.java*](src/module-info.java) relate to
renaming the module from *"se1.play"* to *"se1.bestellsystem"*.

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
# show differences of file 'src/module-info.java' between the current and the incoming branch
git diff main se1-patch-repository/se1-patch -- src/module-info.java

git diff main se1-patch-repository/se1-patch -- src/application/package-info.java

git diff main se1-patch-repository/se1-patch -- resources/application.properties

git diff main se1-patch-repository/se1-patch -- .env.sh
```

Incoming changes fetched from the remote branch *se1-patch-repository/se1-patch*
can be applied by `git merge`. During the merge, git integrates (merges) changes
from the *merge-branch* into the *current branch* (here: *main*). Since the patch
was created outside the commit-history of the *main* branch, unrelated commit
histories must be allowed.

```sh
# merge fetched branch into 'main' branch
git merge se1-patch-repository/se1-patch --allow-unrelated-histories
```

This merge leads to conflicts, which means that *git* was not able to integrate
files and text from the *incoming* branch into the *current* (main) branch
(se1-patch):

```
Auto-merging .env.sh
CONFLICT (add/add): Merge conflict in .env.sh
Auto-merging .vscode/launch.json
CONFLICT (add/add): Merge conflict in .vscode/launch.json
Auto-merging README.md
CONFLICT (add/add): Merge conflict in README.md
Auto-merging resources/application.properties
CONFLICT (add/add): Merge conflict in resources/application.properties
Auto-merging src/application/package-info.java
CONFLICT (add/add): Merge conflict in src/application/package-info.java
Auto-merging src/module-info.java
CONFLICT (add/add): Merge conflict in src/module-info.java
Automatic merge failed; fix conflicts and then commit the result
```

Open file: `src/module-info.java` to see the insertions *git* has made to
mark conflicts:

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

Since *git* has inserted markers into files, the Java compiler will
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

For *git*, the project is in a state with: *unmerged paths*, which means
the merge has not been completed - the merge is still *open*.

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
in an IDE and inserting the correct text.

If the manual merge fails or ends in *"merge hell"*, an *"open merge"* can
always be reset restoring the status of the project before the merge:

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

Nothing was done, the name *"SE-1 Play"* still appears.

Repeat the merge and resolve all conflicts. Modern IDE provide support by offering
selections: *Accept Current* or *Accept Incoming* changes.

Insert your name as `Author` in file
[*src/application/package-info.java*](src/application/package-info.java).

Since a merge injects the full commit-historie of the incoming branch
into the merge-branch, it is useful to *"squash"* incoming commits into one.

```sh
# merge fetched branch into 'main' branch (squash commits from merged branch)
git merge --squash se1-patch-repository/se1-patch --allow-unrelated-histories
```

Make sure to resolve all merge conflicts in files.
Probe for remaining open conflicts:

```sh
git diff --check                    # show remaining open merge conflicts in files
                                    # output must be empty, no "leftover conflict marker"
```
```
.env.sh:143: leftover conflict marker
.env.sh:145: leftover conflict marker
.env.sh:147: leftover conflict marker
.vscode/launch.json:9: leftover conflict marker
.vscode/launch.json:16: leftover conflict marker
.vscode/launch.json:23: leftover conflict marker
resources/application.properties:11: leftover conflict marker
resources/application.properties:15: leftover conflict marker
src/application/package-info.java:20: leftover conflict marker
src/module-info.java:2: leftover conflict marker
src/module-info.java:5: leftover conflict marker
src/module-info.java:8: leftover conflict marker
src/module-info.java:20: leftover conflict marker
src/module-info.java:22: leftover conflict marker
src/module-info.java:24: leftover conflict marker
```

Resolve conflicts until the `git diff --check` output is empty.

Make sure the project compiles and runs after all conflicts have been resolved.

```sh
mk compile run A BB CCC             # the project compiles and runs
```

With the patch, the new name of the project appears: *"SE-1 Bestellsystem"*.

```
java application.Runtime A BB CCC
Hello, "SE-1 Bestellsystem" (application.Application)
- arg: A
- arg: BB
- arg: CCC
```

If the project compiles and runs, the *open merge* can be committed:

```sh
git status                          # show files with modifications (red)

git add .                           # prepare ("stage") merge commit

git status                          # show staged files (green)

# commit ("record") staged files in the local repository
git commit -m "merge commit 'se1-patch'"

git log --oneline                   # show the new merge-commit on top of 'main'-branch ('HEAD')
```

The *git*-log shows the new merge-commit on top of the *main*-branch (*'HEAD'*).
It also shows the commit history inherited with the merge:

```
79d62fd (HEAD -> main) merge commit 'se1-patch'     <-- new merge commit
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
mk build                # compile, compile-tests, run-tests and package

# run the packaged 'jar'-file
java -jar bin/application-1.0.0-SNAPSHOT.jar A BB CCC DDDD
```

The correct name of the project: *"SE-1 Bestellsystem"* appears as
it is defined in:
[*application.properties*](resources/application.properties).

```
Hello, "SE-1 Bestellsystem" (application.Application)
- arg: A
- arg: BB
- arg: CCC
- arg: DDDD
```

Create the Java documentation:

```sh
mk javadoc              # create javadoc
```

Open the documentation: `docs/index.html` and show that your name appears
on pages as `Author: <your name>`. The name is defined in file
[*application.package-info.java*](src/application/package-info.java).


&nbsp;

## 4. Branch Clean Up

Remove remote branches from project that are no longer needed.

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
git branch -avv                     # show branches again
```

Remote branches are gone. Only the *main*-branch remains in the local repository.

```
* main                      31292ba merge commit remote branch se1-patch-repository/se1-patch
```

Next, remove links to remote repositories that are no longer needed:

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

```sh
git remote remove se1-play          # remove remote 'se1-play'
git remote -v                       # show remote repository links
```
```
se1-patch-repository    https://github.com/sgra64/se1-bestellsystem.git (fetch)
se1-patch-repository    https://github.com/sgra64/se1-bestellsystem.git (push)
```

Link `se1-play` is removed. Link `se1-patch-repository` can be kept
for future patches.

Finally, the commit history inherited from merges is cleaned up.

```sh
git log --oneline
```
```
79d62fd (HEAD -> main) merge commit 'se1-patch'
05d74c0 add patch files, update README.md
187123e se1-play main branch
30c6a74 update .env.sh, in wipe keep libs link
b2dceeb update Runtime.java with new bean implementation
60269fb add folders: src tests resources, update README.md
e04da83 add .env.sh, update README.md
8e2a84c add .vscode settings, update README.md
3a20b05 initial commit: .gitignore, README.md
```

All commits will be collapsed into one new commit on the *main*-branch
as initial commit of the new project:

```sh
# collapse commits inherited from merges into one commit
git reset $(git commit-tree HEAD^{tree} -m "se1-bestellsystem base commit")

git log --oneline
```

The commit log shows only one new commit that contains the current
project state:

```
779b7b7 (HEAD -> main) se1-bestellsystem base commit
```


&nbsp;

## 5. Check Project into Own Repository

Create a new project with name: *"se1-bestellsystem"* in your
[*BHT GitLab*](https://gitlab.bht-berlin.de/)
(or other) repository.

Make sure, your `public ssh key` is registered in your account.

Obtain the *ssh*-repository URL from the GitLab project site, e.g.
`git@gitlab.bht-berlin.de:<your-id>/se1.bestellsystem.git`
and register the name `origin` as new remote repository:

```sh
# register the remote repository URL under the name 'origin'
# make sure to replace the '<...>' with your account id
git remote add origin git@gitlab.bht-berlin.de:<...>/se1.bestellsystem.git

# show the new remote URL
git remote -v
```

The new URL is registered under name `origin`.

```
origin  git@gitlab.bht-berlin.de/se1-bestellsystem.git (fetch)
origin  git@gitlab.bht-berlin.de/se1-bestellsystem.git (push)
se1-patch-repository    https://github.com/sgra64/se1-bestellsystem.git (fetch)
se1-patch-repository    https://github.com/sgra64/se1-bestellsystem.git (push)
```

Show the new remote URL in file `.git/config`:

```
[core]
        repositoryformatversion = 0
        filemode = false
        bare = false
        logallrefupdates = true
        ignorecase = true
[remote "origin"]               <-- new remote URL
        url = git@gitlab.bht-berlin.de/se1-bestellsystem.git
        fetch = +refs/heads/*:refs/remotes/origin/*
[remote "se1-patch-repository"]
        url = https://github.com/sgra64/se1-bestellsystem.git
        fetch = +refs/heads/*:refs/remotes/se1-patch-repository/*
```

Finally, the main branch can be pushed to the remote `origin`:

```sh
git push --set-upstream origin main     # push branch 'main' to remote 'origin'

# or as short version:
git push -u origin main                 # short for pushing branch
```

If the *push* fails with an error:

```
$ git push

 ! [rejected]        main -> main (fetch first)
error: failed to push some refs to 'git@gitlab.bht-berlin.de/se1-bestellsystem.git'
hint: Updates were rejected because the remote contains work that you do
hint: not have locally. This is usually caused by another repository pushing
hint: to the same ref. You may want to first merge the remote changes (e.g.,
hint: 'git pull') before pushing again.
```

the branch in the remote repository has commits that are not local.
Unlike *GitHub*, *GitLab* does not create a new repository *empty*, but creates
an initial commit causing the
[*push conflict*](https://charlesreid1.com/wiki/Git/Resolving_Push_Conflicts).

Resolve the *push conflict* by *pulling* the missing commit from the remote.

```sh
git pull                # pull and merge commits from the remote in case of 'pus conflict'

# repeat pushing the branch to 'origin'
git push --set-upstream origin main
```


&nbsp;

## 6. *Setup* and *Build* Automation

The *setup* process can be automated by summarizing commands in a
`setup()` function. The function verifies it is executed in an
empty project directory named *"se1-bestellsystem"*:

```sh
# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
# setup() function automates the 'se1-bestellsystem' assembly starting with
# creating an empty project directory 'se1-bestellsystem'.
# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
function setup() {
    local current_dir=$(basename $(pwd))
    [ ! "$current_dir" = "se1-bestellsystem" -o "$(ls -A)" ] && \
        echo "setup() must run in an empty directory named 'se1-bestellsystem'" && \
        return

    # create local git repository in the project directory
    git init

    # set remote and pull 'main' branch from 'se1-play'
    git remote add se1-play https://github.com/sgra64/se1-play.git
    git pull se1-play main

    # pull 'libs' branch from 'se1-play' repository
    mkdir branches
    cd branches
    git clone -b libs --single-branch https://github.com/sgra64/se1-play.git libs
    cd ..

    # fetch 'se1-patch'
    git remote add se1-patch-repository https://github.com/sgra64/se1-bestellsystem.git
    git fetch se1-patch-repository se1-patch

    # merge patch with strategy to accept all incoming changes ("theirs")
    git merge --squash se1-patch-repository/se1-patch \
        --no-commit \
        --allow-unrelated-histories \
        --strategy-option theirs
    # 
    # get README.md from 'se1-bestellsystem' main branch
    echo "fetching README.md from se1-bestellsystem main branch"
    curl --output README.md \
        "https://raw.githubusercontent.com/sgra64/se1-bestellsystem/refs/heads/main/README.md" \
        >/dev/null 2>&1 || true
    # 
    git add .                       # stage and commit the open merge
    git commit -m "merge commit 'se1-patch'"

    # cleanup: remove fetched branches, remote links, collapse commit history
    git branch -dr se1-play/main
    git branch -dr se1-patch-repository/se1-patch
    git remote remove se1-play

    # collapse commits inherited from merges into one commit
    git reset $(git commit-tree HEAD^{tree} -m "se1-bestellsystem base commit")

    source .env.sh                  # source the project
    cd .
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
