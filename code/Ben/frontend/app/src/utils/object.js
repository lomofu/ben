export function isNull(val) {
  return val === null;
}

export function isUndefined(val) {
  return val === undefined || typeof val === "undefined";
}

export function isEmpty(val) {
  return isNull(val) || isUndefined(val);
}

export function hasNoText(str) {
  return isNull(str) || isUndefined(str) || str === "";
}
