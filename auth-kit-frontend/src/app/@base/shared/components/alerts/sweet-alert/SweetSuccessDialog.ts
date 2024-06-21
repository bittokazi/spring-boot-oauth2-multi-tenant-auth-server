export function SweetSuccessDialog(options?) {
  return function (
    target: any,
    propertyKey: string,
    descriptor: PropertyDescriptor
  ) {
    let config = {
      title: 'Success',
      text: 'Successfully Completed!',
    };
    if (options) {
      Object.keys(options).forEach((x) => (config[x] = options[x]));
    }
    const originalMethod = descriptor.value;
    descriptor.value = function (...args) {
      let Swal = window['Swal'];
      Swal.fire(config.title, config.text, 'success').then(() => {
        originalMethod.apply(this, args);
      });
      return {};
    };
    return descriptor;
  };
}
