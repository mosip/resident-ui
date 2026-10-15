#!/bin/bash
set -e

java --version
mvn --version

# Rancher mounts the equivalent of resident.properties (URLs, secrets, UIN/VID test data) as a
# ConfigMap/Secret file — not baked into the image, since it's gitignored and holds real
# credentials. Drop it where Maven's own resource handling already expects it, so `mvn test`
# picks it up exactly like a local run reading src/test/resources/config/resident.properties.
RESIDENT_CONFIG_PATH="${RESIDENT_CONFIG_PATH:-/home/mosip/config/resident.properties}"
if [ -f "$RESIDENT_CONFIG_PATH" ]; then
    cp "$RESIDENT_CONFIG_PATH" src/test/resources/config/resident.properties
    echo "Mounted config applied from $RESIDENT_CONFIG_PATH"
else
    echo "No mounted config found at $RESIDENT_CONFIG_PATH — falling back to whatever's baked into the image (if anything)."
fi

# -o: dependencies were already resolved into this image's .m2 at build time.
# Exit code propagates mvn's own (non-zero on any test failure), so Rancher sees a failed Job.
mvn -o test -Dgpg.skip=true
