<script>
import { checkPermission } from "../../utils/auth/auth";
export default {
  name: "Permission",
  functional: true,
  props: {
    perm: {
      type: String,
      required: true
    }
  },
  render(createElement, context) {
    const { props, scopedSlots } = context;
    return checkPermission(props.perm) ? scopedSlots.default() : null;
  }
};
</script>
