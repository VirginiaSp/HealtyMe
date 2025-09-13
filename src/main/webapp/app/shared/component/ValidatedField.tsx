/**
 * Custom ValidatedField component to replace react-jhipster ValidatedField
 */
import React, { useState, useEffect } from 'react';
import { Input, FormGroup, Label, FormFeedback, InputProps } from 'reactstrap';
import { validateField } from '../util/validation-utils';

interface ValidatedFieldProps {
  name: string;
  label?: string;
  placeholder?: string;
  type?: InputProps['type'];
  validate?: any;
  onChange?: (event: React.ChangeEvent<HTMLInputElement>) => void;
  'data-cy'?: string;
  id?: string;
  className?: string;
  value?: string;
  defaultValue?: string;
  required?: boolean;
  autoFocus?: boolean;
  children?: React.ReactNode;
  check?: boolean;
  register?: any;
  error?: any;
  isTouched?: any;
}

export const ValidatedField: React.FC<ValidatedFieldProps> = ({
  name,
  label,
  placeholder,
  type = 'text',
  validate,
  onChange,
  'data-cy': dataCy,
  id,
  className,
  value,
  defaultValue,
  required,
  autoFocus,
  children,
  check,
  register,
  error,
  isTouched,
  ...props
}) => {
  const [fieldValue, setFieldValue] = useState(value || defaultValue || '');
  const [fieldError, setFieldError] = useState<string | null>(null);
  const [touched, setTouched] = useState(false);

  useEffect(() => {
    if (value !== undefined) {
      setFieldValue(value);
    }
  }, [value]);

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const newValue = event.target.value;
    setFieldValue(newValue);

    if (validate && touched) {
      const validationError = validateField(newValue, validate);
      setFieldError(validationError);
    }

    if (onChange) {
      onChange(event);
    }
  };

  const handleBlur = () => {
    setTouched(true);
    if (validate) {
      const validationError = validateField(fieldValue, validate);
      setFieldError(validationError);
    }
  };

  const isInvalid = touched && !!fieldError;

  // Handle select fields
  if (type === 'select') {
    return (
      <FormGroup className={className}>
        {label && <Label for={id || name}>{label}</Label>}
        <Input
          id={id || name}
          name={name}
          type="select"
          value={fieldValue}
          onChange={handleChange}
          onBlur={handleBlur}
          invalid={isInvalid}
          data-cy={dataCy}
          {...props}
        >
          {children}
        </Input>
        {isInvalid && <FormFeedback>{fieldError}</FormFeedback>}
      </FormGroup>
    );
  }

  return (
    <FormGroup className={className} check={check}>
      {label && (
        <Label for={id || name} check={check}>
          {label}
        </Label>
      )}
      <Input
        id={id || name}
        name={name}
        type={type}
        placeholder={placeholder}
        value={fieldValue}
        onChange={handleChange}
        onBlur={handleBlur}
        invalid={isInvalid}
        data-cy={dataCy}
        required={required}
        autoFocus={autoFocus}
        {...props}
      />
      {isInvalid && <FormFeedback>{fieldError}</FormFeedback>}
    </FormGroup>
  );
};
