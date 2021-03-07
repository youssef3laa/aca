
import {extend } from 'vee-validate';
import { required, min_value, max } from 'vee-validate/dist/rules';
import { localize } from 'vee-validate';

extend('required', {
  ...required
})

extend('min_value', {
    ...min_value
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
        required: 'Field is required',
        min: 'this field must have no less than {length} characters',
        max: (_, { length }) => `this field must have no more than ${length} characters`,
        minmax: (field, { min, max }) =>  `${field} must be between ${min} and ${max}`,
        min_value: (field, { min }) =>  `${field} must have a value of more than ${min - 1}`
      },
      names: {
        username: "Username",
        password: "Password"
      }
    },
    ar: {
      messages: {
        required: 'لا يمكن ان يكون فارغ',
        min: '{_field_} هذا الحقل يجب ان يكون اكبر من  {length} حرف',
        max: (_, { length }) => `this field must have no more than ${length} characters`,
        minmax: (field, { min, max }) =>  `${field} يجب ان يكون ${min} بين ${max}`,
        min_value: (field, { min }) =>  `${field} يجب ان يكون أكبر من ${min - 1}`
      },
      names: {
        username: "إسم المستخدم",
        password: "كلمه المرور"
      }
    }
});

localize("ar")