#!/bin/sh
here="`dirname \"$0\"`"
echo "cd-ing to $here"
cd "$here"
./gradlew setupDecompWorkspace eclipse