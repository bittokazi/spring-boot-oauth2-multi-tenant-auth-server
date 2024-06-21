export default function FormErrorSetter(form, errors) {
  for (const fieldName in errors) {
    const serverErrors = errors[fieldName];
    const err = {};
    for (const serverError of serverErrors) {
      err[serverError] = true;
    }
    const control = form.get(fieldName);
    control.setErrors(err);
    control.markAsDirty();
  }
}
