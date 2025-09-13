/**
 * Validation utilities to replace react-jhipster validation functions
 */

export const isEmail = (value: string): boolean => {
  const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  return emailRegex.test(value);
};

export const isRequired = (value: any): boolean => {
  if (value === undefined || value === null) return false;
  if (typeof value === 'string') return value.trim().length > 0;
  return true;
};

export const minLength = (value: string, min: number): boolean => {
  return value && value.length >= min;
};

export const maxLength = (value: string, max: number): boolean => {
  return !value || value.length <= max;
};

export const pattern = (value: string, regex: RegExp): boolean => {
  return !value || regex.test(value);
};

export const validateField = (value: any, rules: any): string | null => {
  if (rules.required && !isRequired(value)) {
    return rules.required.message || 'This field is required';
  }

  if (value && rules.minLength && !minLength(value, rules.minLength.value)) {
    return rules.minLength.message || `Minimum length is ${rules.minLength.value}`;
  }

  if (value && rules.maxLength && !maxLength(value, rules.maxLength.value)) {
    return rules.maxLength.message || `Maximum length is ${rules.maxLength.value}`;
  }

  if (value && rules.pattern && !pattern(value, rules.pattern.value)) {
    return rules.pattern.message || 'Invalid format';
  }

  if (value && rules.validate && typeof rules.validate === 'function') {
    const result = rules.validate(value);
    if (result !== true) {
      return result || 'Invalid value';
    }
  }

  return null;
};
