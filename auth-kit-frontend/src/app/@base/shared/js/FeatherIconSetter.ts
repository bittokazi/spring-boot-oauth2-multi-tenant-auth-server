export const FeatherIconSetter = (options?) => {
  setTimeout(
    () => {
      if (window['feather']) {
        window['feather'].replace({
          width: options && options.width ? options.width : 14,
          height: options && options.height ? options.height : 14,
        });
      }
    },
    options && options.delay ? options.delay : 200
  );
};

export function FeatherIconSetterInjector(options?) {
  return function (
    target: any,
    propertyKey: string,
    descriptor: PropertyDescriptor
  ) {
    const originalMethod = descriptor.value;
    descriptor.value = function (...args) {
      const result = originalMethod.apply(this, args);
      FeatherIconSetter(options);
      return result;
    };
    return descriptor;
  };
}
