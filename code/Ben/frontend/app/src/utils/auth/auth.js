import vue from "../../main";

export function getCurrentAccountPermission() {
  return vue.$store.getters.getPermission;
}

export function checkPermission(perm) {
  return getCurrentAccountPermission().some(e => e === perm);
}

export function checkStorePermission(permlist, perm) {
  return permlist.some(e => e === perm);
}
