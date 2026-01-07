#!/bin/bash

psql -f /docker-entrypoint-initdb.d/demo-small-en-20170815.sql
