export function generateMenu() {
  return [
    {
      active: false,
      path: '/dashboard',
      icon: '',
      title: 'Dashboard',
      enabled: true,
      enabledInAdmin: 'true',

      subMenu: [],
    },
    {
      active: false,
      path: '/dashboard/tenants',
      icon: '',
      title: 'Tenants',
      enabled: true,
      enabledInAdmin: 'true',

      subMenu: [
        {
          active: false,
          path: '/dashboard/tenants',
          icon: '',
          title: 'All Tenants',
          enabled: true,
          enabledInAdmin: 'true',

          subMenu: [],
        },
        {
          active: false,
          path: '/dashboard/tenants/add',
          icon: '',
          title: 'Add Tenant',
          enabled: true,
          enabledInAdmin: 'true',

          subMenu: [],
        },
        {
          active: false,
          path: '/dashboard/tenants/?/update',
          icon: '',
          title: 'Update Tenant',
          enabled: true,
          enabledInAdmin: 'true',
          show: false,
          subMenu: [],
        },
      ],
    },
    {
      active: false,
      path: '/dashboard/clients',
      icon: '',
      title: 'Clients',
      enabled: true,
      enabledInAdmin: 'true',

      subMenu: [
        {
          active: false,
          path: '/dashboard/clients',
          icon: '',
          title: 'All Clients',
          enabled: true,
          enabledInAdmin: 'true',

          subMenu: [],
        },
        {
          active: false,
          path: '/dashboard/clients/add',
          icon: '',
          title: 'Add Client',
          enabled: true,
          enabledInAdmin: 'true',

          subMenu: [],
        },
        {
          active: false,
          path: '/dashboard/clients/?/update',
          icon: '',
          title: 'Update Client',
          enabled: true,
          enabledInAdmin: 'true',
          show: false,
          subMenu: [],
        },
      ],
    },
    {
      active: false,
      path: '/dashboard/users',
      icon: '',
      title: 'Users',
      enabled: true,
      enabledInAdmin: 'true',

      subMenu: [
        {
          active: false,
          path: '/dashboard/users',
          icon: '',
          title: 'All Users',
          enabled: true,
          enabledInAdmin: 'true',

          subMenu: [],
        },
        {
          active: false,
          path: '/dashboard/users/add',
          icon: '',
          title: 'Add User',
          enabled: true,
          enabledInAdmin: 'true',

          subMenu: [],
        },
        {
          active: false,
          path: '/dashboard/users/?/update',
          icon: '',
          title: 'Update User',
          enabled: true,
          enabledInAdmin: 'true',
          show: false,
          subMenu: [],
        },
      ],
    },
    {
      active: false,
      path: '/dashboard/roles',
      icon: '',
      title: 'Roles',
      enabled: true,
      enabledInAdmin: 'true',

      subMenu: [
        {
          active: false,
          path: '/dashboard/roles',
          icon: '',
          title: 'All Roles',
          enabled: true,
          enabledInAdmin: 'true',

          subMenu: [],
        },
        {
          active: false,
          path: '/dashboard/roles/add',
          icon: '',
          title: 'Add Role',
          enabled: true,
          enabledInAdmin: 'true',

          subMenu: [],
        },
        {
          active: false,
          path: '/dashboard/roles/?/update',
          icon: '',
          title: 'Update Role',
          enabled: true,
          enabledInAdmin: 'true',
          show: false,
          subMenu: [],
        },
      ],
    },
  ];
}
