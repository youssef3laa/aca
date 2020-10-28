import {  extend } from 'vee-validate';
import { required, max } from 'vee-validate/dist/rules';
import { localize } from 'vee-validate';

extend('required', {
  ...required,
})

extend('max', {
  ...max
})

extend('minmax', {
    validate(value, { min, max }) {
        if(value.length >= min && value.length <= max){
            return true;
        }
    },
    params: ['min', 'max']
});


localize({
    en: {
      messages: {
        required: '{_field_} is required',
        min: 'this field must have no less than {length} characters',
        max: (_, { length }) => `this field must have no more than ${length} characters`,
        minmax: (field, { min, max }) =>  `${field} must be between ${min} and ${max}`
      },
      names: {
        email: "Email",
        password: "Password"
      }
    },
    ar: {
      messages: {
        required: '{_field_} لا يمكن ان يكون فارغ',
        min: '{_field_} هذا الحقل يجب ان يكون اكبر من  {length} حرف',
        max: (_, { length }) => `this field must have no more than ${length} characters`,
        minmax: (field, { min, max }) =>  `${field} يجب ان يكون ${min} بين ${max}`
      },
      names: {
        email: "Email",
        password: "Password"
      }
    }
});