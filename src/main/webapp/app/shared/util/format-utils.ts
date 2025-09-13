/**
 * Text formatting utilities to replace react-jhipster TextFormat
 */
import { format } from 'date-fns';

export const formatDate = (date: Date | string | number, formatString: string = 'dd/MM/yyyy'): string => {
  if (!date) return '';

  try {
    const dateObj = date instanceof Date ? date : new Date(date);
    return format(dateObj, formatString);
  } catch (error) {
    return String(date);
  }
};

export const formatDateTime = (date: Date | string | number): string => {
  return formatDate(date, 'dd/MM/yyyy HH:mm');
};

export const formatNumber = (num: number, options?: Intl.NumberFormatOptions): string => {
  if (num == null) return '';
  return new Intl.NumberFormat('en-US', options).format(num);
};

export const formatCurrency = (amount: number, currency: string = 'USD'): string => {
  return formatNumber(amount, { style: 'currency', currency });
};
