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
  blankOnInvalid?: boolean;
}

export const TextFormat: React.FC<TextFormatProps> = ({ value, type = 'date', format, currency = 'USD', blankOnInvalid }) => {
  if (value == null) return null;

  let formattedValue: string;

  try {
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
  } catch (error) {
    if (blankOnInvalid) {
      return null;
    }
    formattedValue = String(value);
  }

  return <span>{formattedValue}</span>;
};
