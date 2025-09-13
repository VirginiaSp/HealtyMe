import React from 'react';
import { FormFeedback, FormGroup, Input, Label } from 'reactstrap';
import { translate } from '../util/translation-utils';

/**
 * Validation rules interface
 */
interface ValidationRules {
  required?: boolean | { value: boolean; message: string };
  minlength?: { value: number; message: string };
  maxlength?: { value: number; message: string };
  minLength?: { value: number; message: string }; // Support both for compatibility
  maxLength?: { value: number; message: string }; // Support both for compatibility
  min?: { value: number; message: string };
  max?: { value: number; message: string };
  pattern?: { value: RegExp; message: string };
  validate?: (value: any) => boolean | string;
}

/**
 * ValidatedField props interface
 */
interface ValidatedFieldProps {
  name: string;
  id?: string;
  label?: string;
  type?: string;
  placeholder?: string;
  value?: any;
  defaultValue?: any;
  onChange?: (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => void;
  onBlur?: (e: React.FocusEvent<HTMLInputElement | HTMLSelectElement>) => void;
  validate?: ValidationRules;
  readOnly?: boolean;
  required?: boolean;
  children?: React.ReactNode;
  'data-cy'?: string;
  className?: string;
  error?: string;
  tag?: any;
  register?: any; // For react-hook-form integration
}

/**
 * ValidatedField component to replace react-jhipster ValidatedField
 */
export const ValidatedField: React.FC<ValidatedFieldProps> = ({
  name,
  id,
  label,
  type = 'text',
  placeholder,
  value,
  defaultValue,
  onChange,
  onBlur,
  validate,
  readOnly,
  required,
  children,
  'data-cy': dataCy,
  className,
  error,
  tag = Input,
  register,
  ...props
}) => {
  const fieldId = id || name;
  const isSelect = type === 'select';
  const Component = tag;

  // Extract required from validate if present
  const isRequired = required || validate?.required === true || (typeof validate?.required === 'object' && validate.required.value);

  // Get error message
  const errorMessage = error || (typeof validate?.required === 'object' && !value && validate.required.message);

  const fieldProps = {
    id: fieldId,
    name,
    type: isSelect ? undefined : type,
    placeholder: placeholder || (label ? translate(label) : undefined),
    value,
    defaultValue,
    onChange,
    onBlur,
    readOnly,
    required: isRequired,
    'data-cy': dataCy,
    className: className ? `${className}${errorMessage ? ' is-invalid' : ''}` : errorMessage ? 'is-invalid' : undefined,
    invalid: !!errorMessage,
    ...props,
  };

  if (register) {
    // For react-hook-form integration
    const registerProps = register(name, validate);
    Object.assign(fieldProps, registerProps);
  }

  return (
    <FormGroup>
      {label && (
        <Label for={fieldId}>
          {translate(label)}
          {isRequired && <span className="text-danger"> *</span>}
        </Label>
      )}
      <Component {...fieldProps}>{children}</Component>
      {errorMessage && <FormFeedback>{errorMessage}</FormFeedback>}
    </FormGroup>
  );
};

/**
 * ValidatedForm props interface
 */
interface ValidatedFormProps {
  defaultValues?: Record<string, any>;
  onSubmit: (data: any) => void;
  children: React.ReactNode;
  className?: string;
  id?: string;
}

/**
 * ValidatedForm component to replace react-jhipster ValidatedForm
 */
export const ValidatedForm: React.FC<ValidatedFormProps> = ({ defaultValues = {}, onSubmit, children, className, id, ...props }) => {
  const [formData, setFormData] = React.useState(defaultValues);
  const [errors, setErrors] = React.useState<Record<string, string>>({});

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit(formData);
  };

  const handleFieldChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value, type } = e.target;
    const processedValue = type === 'checkbox' ? (e.target as HTMLInputElement).checked : value;

    setFormData(prev => ({
      ...prev,
      [name]: processedValue,
    }));
  };

  return (
    <form onSubmit={handleSubmit} className={className} id={id} {...props}>
      {React.Children.map(children, child => {
        if (React.isValidElement(child) && child.type === ValidatedField) {
          return React.cloneElement(child as any, {
            value: formData[child.props.name] || '',
            onChange: handleFieldChange,
            error: errors[child.props.name],
          });
        }
        return child;
      })}
    </form>
  );
};

/**
 * ValidatedBlobField props interface
 */
interface ValidatedBlobFieldProps extends ValidatedFieldProps {
  accept?: string;
}

/**
 * ValidatedBlobField component to replace react-jhipster ValidatedBlobField
 */
export const ValidatedBlobField: React.FC<ValidatedBlobFieldProps> = props => {
  return <ValidatedField {...props} type="file" />;
};
