/**
 * Custom TextFormat component to replace react-jhipster TextFormat
 */
import React from 'react';
import { formatDate, formatDateTime, formatNumber, formatCurrency } from '../util/format-utils';

interface TextFormatProps {
  value: any;
  type?: 'date' | 'datetime' | 'number' | 'currency';
  format?: string;
  currency?: string;
}

export const TextFormat: React.FC<TextFormatProps> = ({ value, type = 'date', format, currency = 'USD' }) => {
  if (value == null) return null;

  let formattedValue: string;

  switch (type) {
    case 'date':
      formattedValue = formatDate(value, format);
      break;
    case 'datetime':
      formattedValue = formatDateTime(value);
      break;
    case 'number':
      formattedValue = formatNumber(value);
      break;
    case 'currency':
      formattedValue = formatCurrency(value, currency);
      break;
    default:
      formattedValue = String(value);
  }

  return <span>{formattedValue}</span>;
};
