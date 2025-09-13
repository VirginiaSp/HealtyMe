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
  ...props
}) => {
  const [fieldValue, setFieldValue] = useState(value || defaultValue || '');
  const [error, setError] = useState<string | null>(null);
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
      setError(validationError);
    }

    if (onChange) {
      onChange(event);
    }
  };

  const handleBlur = () => {
    setTouched(true);
    if (validate) {
      const validationError = validateField(fieldValue, validate);
      setError(validationError);
    }
  };

  const isInvalid = touched && !!error;

  return (
    <FormGroup className={className}>
      {label && <Label for={id || name}>{label}</Label>}
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
        {...props}
      />
      {isInvalid && <FormFeedback>{error}</FormFeedback>}
    </FormGroup>
  );
};
