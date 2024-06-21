#!/usr/bin/env bash
 
# Exit script if command fails or uninitialized variables used
set -euo pipefail
 
# ==================================
# Verify repo is clean
# ==================================
 
# List uncommitted changes and
# check if the output is not empty
if [ -n "$(git status --porcelain)" ]; then
  # Print error message
  printf "\nError: repo has uncommitted changes\n\n"
  # Exit with error code
  exit 1
fi
 
# ==================================
# Get latest version from git tags
# ==================================
 
# List git tags sorted lexicographically
# so version numbers sorted correctly
GIT_TAGS=$(git tag --sort=version:refname)
 
# Get last line of output which returns the
# last tag (most recent version)
GIT_TAG_LATEST=$(echo "$GIT_TAGS" | tail -n 1)
 
# If no tag found, default to v0.0.0
if [ -z "$GIT_TAG_LATEST" ]; then
  GIT_TAG_LATEST="v0.0.0"
fi
 
# Strip prefix 'v' from the tag to easily increment
GIT_TAG_LATEST=$(echo "$GIT_TAG_LATEST" | sed 's/^v//')
 
# ==================================
# Increment version number
# ==================================
 
# Get version type from first argument passed to script
VERSION_TYPE="${1-}"
VERSION_NEXT=""
 
if [ "$VERSION_TYPE" = "patch" ]; then
  # Increment patch version
  VERSION_NEXT="$(echo "$GIT_TAG_LATEST" | awk -F. '{$3++; print $1"."$2"."$3}')"
elif [ "$VERSION_TYPE" = "minor" ]; then
  # Increment minor version
  VERSION_NEXT="$(echo "$GIT_TAG_LATEST" | awk -F. '{$2++; $3=0; print $1"."$2"."$3}')"
elif [ "$VERSION_TYPE" = "major" ]; then
  # Increment major version
  VERSION_NEXT="$(echo "$GIT_TAG_LATEST" | awk -F. '{$1++; $2=0; $3=0; print $1"."$2"."$3}')"
else
  # Print error for unknown versioning type
  printf "\nError: invalid VERSION_TYPE arg passed, must be 'patch', 'minor' or 'major'\n\n"
  # Exit with error code
  exit 1
fi
 
# ==================================
# Update manifest file (optional)
# assuming Rust project with Cargo.toml
# modify this as needed for your project
# ==================================
 
# Update version in package.json
#sed -i "s/\"version\": \".*\"/\"version\": \"$VERSION_NEXT\"/" package.json

# Update version in info.json
sed -i "s/\"version\": \".*\"/\"version\": \"$VERSION_NEXT\"/" info.json
 
# Commit the changes
git add .
git commit -am "build: bump info.json version - v$VERSION_NEXT :bookmark:"
 
# ==================================
# Create git tag for new version
# ==================================
 
# Create an annotated tag
git tag -a "v$VERSION_NEXT" -m "Release: v$VERSION_NEXT"
 
# Optional: push commits and tag to remote 'main' branch
git push origin main --follow-tags