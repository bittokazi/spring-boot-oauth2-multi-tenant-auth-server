let $ = window['$'];

export function DashboardMenuToggle() {
  let $body = $('body');
  $.app.menu.toggle();

  setTimeout(function () {
    $(window).trigger('resize');
  }, 200);

  if ($('#collapse-sidebar-switch').length > 0) {
    setTimeout(function () {
      if ($body.hasClass('menu-expanded') || $body.hasClass('menu-open')) {
        $('#collapse-sidebar-switch').prop('checked', false);
      } else {
        $('#collapse-sidebar-switch').prop('checked', true);
      }
    }, 50);
  }
}

export function CloseOpenMenu() {
  if ($('.collapse-toggle-icon').css('display') == 'none') {
    $('.sidenav-overlay').click();
  }
}
