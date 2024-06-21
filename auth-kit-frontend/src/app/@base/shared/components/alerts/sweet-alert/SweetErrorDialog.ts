export function SweetErrorDialog(options?) {
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
      Swal.fire({
        type: 'error',
        title: config.title,
        text: config.text,
      });
      const result = originalMethod.apply(this, args);
      return result;
    };
    return descriptor;
  };
}
