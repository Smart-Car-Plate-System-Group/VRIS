#!/usr/bin/env sh
set -e
cd "$(dirname "$0")"
javac -encoding UTF-8 SmartCarPlateSystem.java
java SmartCarPlateSystem
