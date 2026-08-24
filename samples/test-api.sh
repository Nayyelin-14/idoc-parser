#!/usr/bin/env bash
# IDoc Parser Service - end-to-end API test suite.
# Usage:  bash samples/test-api.sh          (app must be on :8000)
#         BASE_URL=http://localhost:9000 bash samples/test-api.sh

set -u
BASE_URL="${BASE_URL:-http://localhost:8000}"
API="$BASE_URL/api/parser-service"
DIR="$(cd "$(dirname "$0")" && pwd)"
PASS=0; FAIL=0

check() { # $1=name $2=condition (0=ok)
  if [ "$2" -eq 0 ]; then echo "PASS: $1"; PASS=$((PASS+1)); else echo "FAIL: $1"; FAIL=$((FAIL+1)); fi
}

echo "== 1. Health =="
H=$(curl -sf "$API/health") ; check "health returns UP" $?
echo "$H" | grep -q '"status":"UP"' ; check "status UP" $?

echo "== 2. Config schema =="
curl -sf "$API/config-schema" | grep -q 'schemaMetadata.specVersion' ; check "config-schema lists options" $?

echo "== 3. Execute with IDoc content in JSON body =="
R=$(curl -sf -X POST "$API/execute" -H 'Content-Type: application/json' \
  -d "{\"input\":\"BEGIN_IDOC TEST\\n  BEGIN_SEGMENT E2EDK01005\\n    SEGMENTTYPE E1EDK01\\n    LEVEL 01\\n    STATUS MANDATORY\\n    BEGIN_FIELDS\\n      NAME ACTION\\n      LENGTH 000003\\n    END_FIELDS\\n  END_SEGMENT\\n\"}")
echo "$R" | python3 -c 'import json,sys; d=json.load(sys.stdin); assert d["success"] and d["schema"]["$defs"]["E2EDK01005"]' ; check "execute parses inline IDoc text" $?

echo "== 4. Parse basic ORDERS05 (flat segments) =="
curl -sf -X POST "$API/parse" \
  -F "file=@$DIR/ORDERS05-basic.txt;type=text/plain" > /tmp/opencode/idoc_basic.json
check "/parse accepts file" $?
grep -q '"E2EDK01005"' /tmp/opencode/idoc_basic.json ; check "schema contains E2EDK01005" $?
grep -q '"EDI_DC40"'   /tmp/opencode/idoc_basic.json ; check "schema contains EDI_DC40 control record" $?

echo "== 5. Parse basic ORDERS05 + standard metadata =="
curl -sf -X POST "$API/parse" \
  -F "file=@$DIR/ORDERS05-basic.txt;type=text/plain" \
  -F "specification=IDoc" -F "specVersion=ORDERS05" -F "code=ORDERS" \
  -F "documentType=Business" -F "category=standard" > /tmp/opencode/idoc_meta.json
grep -q '"ORDERS05"' /tmp/opencode/idoc_meta.json ; check "metadata reflected in schema" $?

echo "== 6. Parse group-heavy sample =="
curl -sf -X POST "$API/parse" \
  -F "file=@$DIR/ORDERS05-groups.txt;type=text/plain" > /tmp/opencode/idoc_groups.json
grep -q '_GRP' /tmp/opencode/idoc_groups.json ; check "group segment (_GRP) generated" $?
python3 - <<'EOF'
import json,sys
s=json.load(open('/tmp/opencode/idoc_groups.json'))['schema']
grp=s['$defs']['E2EDKA10003_GRP']
t2001=grp['properties']['E2EDKT2001']
ok = t2001.get('type')=='array' and 'maxItems' not in t2001
sys.exit(0 if ok else 1)
EOF
check "unbounded child segment is type:array without maxItems" $?

echo "== 7. Projection without projectionId -> expect error =="
CODE=$(curl -s -o /tmp/opencode/idoc_proj.json -w '%{http_code}' -X POST "$API/parse" \
  -F "file=@$DIR/ORDERS05-basic.txt;type=text/plain" -F "category=projection")
[ "$CODE" != "200" ] ; check "rejected (HTTP $CODE)" $?

echo
echo "Results: $PASS passed, $FAIL failed"
[ "$FAIL" -eq 0 ]
