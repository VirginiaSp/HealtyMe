/**
 * Index file for custom components that replace react-jhipster components
 */
import React from 'react';

// Form components
export { ValidatedField } from './ValidatedField';
export { ValidatedForm } from './ValidatedForm';

// Translation components
export { Translate } from './Translate';

// Formatting components
export { TextFormat } from './TextFormat';

// Pagination components
export {
  CustomPagination as Pagination,
  CustomPagination as JhiPagination,
  ItemCount,
  ItemCount as JhiItemCount,
  getPaginationState,
} from './Pagination';

// Utilities
export { translate, Storage } from '../util/translation-utils';
export { isEmail, validateField } from '../util/validation-utils';
export { formatDate, formatDateTime, formatNumber, formatCurrency } from '../util/format-utils';

// Additional exports for compatibility
export { TranslatorContext } from '../../config/translation';

// Sorting utilities
export const getSortState = (location: any, sortField?: string): ISortBaseState => {
  const params = new URLSearchParams(location.search);
  const sort = params.get('sort') || sortField || 'id';
  const [field, order] = sort.split(',');
  return {
    sort: field,
    order: order === 'desc' ? 'desc' : 'asc',
  };
};

// Placeholder exports for metrics components (these would need to be implemented for full metrics support)
export const JvmMemory = () => React.createElement('div', {}, 'JVM Memory metrics not available');
export const JvmThreads = () => React.createElement('div', {}, 'JVM Threads metrics not available');
export const SystemMetrics = () => React.createElement('div', {}, 'System metrics not available');
export const HttpRequestMetrics = () => React.createElement('div', {}, 'HTTP Request metrics not available');
export const CacheMetrics = () => React.createElement('div', {}, 'Cache metrics not available');
export const DatasourceMetrics = () => React.createElement('div', {}, 'Datasource metrics not available');
export const EndpointsRequestsMetrics = () => React.createElement('div', {}, 'Endpoints metrics not available');
export const GarbageCollectorMetrics = () => React.createElement('div', {}, 'GC metrics not available');

// Placeholder types for entity utils compatibility
export interface IPaginationBaseState {
  activePage: number;
  itemsPerPage: number;
  sort: string;
  order: 'asc' | 'desc';
}

export interface ISortBaseState {
  sort: string;
  order: 'asc' | 'desc';
}
