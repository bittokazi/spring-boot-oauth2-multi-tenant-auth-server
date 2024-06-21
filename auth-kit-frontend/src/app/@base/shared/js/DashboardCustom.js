export function DashboardCustom() {
  document
    .getElementById("master-loader-wrapper")
    .classList.add("master-loader-hidden");

  setTimeout(() => {
    document.getElementById("master-loader-wrapper").style.display = "none";
  }, 1500);
  if (window["feather"]) {
    feather.replace({
      width: 14,
      height: 14,
    });
  }
}
