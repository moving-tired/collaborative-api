#!/usr/bin/env bash
set -e

#Uncomment bellow to diagnose dns problems
#cat /etc/resolv.conf
#echo "$(nslookup db)" >&2

/go/bin/toxiproxy -config config.json -host 0.0.0.0