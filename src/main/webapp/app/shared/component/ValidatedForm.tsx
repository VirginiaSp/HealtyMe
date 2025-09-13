/**
 * Custom ValidatedForm component to replace react-jhipster ValidatedForm
 */
import React, { FormEvent, ReactNode } from 'react';
import { Form } from 'reactstrap';

interface ValidatedFormProps {
  id?: string;
  onSubmit: (data: Record<string, any>) => void;
  children: ReactNode;
  className?: string;
}

export const ValidatedForm: React.FC<ValidatedFormProps> = ({ id, onSubmit, children, className, ...props }) => {
  const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    const formData = new FormData(event.currentTarget);
    const data: Record<string, any> = {};

    formData.forEach((value, key) => {
      data[key] = value;
    });

    onSubmit(data);
  };

  return (
    <Form id={id} onSubmit={handleSubmit} className={className} {...props}>
      {children}
    </Form>
  );
};
