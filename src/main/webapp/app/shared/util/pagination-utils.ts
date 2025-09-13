import { ISortBaseState } from './entity-utils';

/**
 * Sort state utilities to replace react-jhipster functionality
 */

/**
 * Get sort state from location for pagination
 */
export const getSortState = (location: Location, defaultSort: string, defaultOrder: 'asc' | 'desc' = 'asc'): ISortBaseState => {
  const params = new URLSearchParams(location.search);
  const sort = params.get('sort');

  if (sort) {
    const sortSplit = sort.split(',');
    return {
      sort: sortSplit[0] || defaultSort,
      order: (sortSplit[1] as 'asc' | 'desc') || defaultOrder,
    };
  }

  return {
    sort: defaultSort,
    order: defaultOrder,
  };
};
