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