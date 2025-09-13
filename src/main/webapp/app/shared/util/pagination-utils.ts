import { ISortBaseState, IPaginationBaseState } from './entity-utils';

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

/**
 * Get pagination state from location
 */
export const getPaginationState = (
  location: { search: string },
  itemsPerPage: number,
  defaultSort: string = 'id',
): IPaginationBaseState => {
  const params = new URLSearchParams(location.search);
  const page = params.get('page');
  const sort = params.get('sort');

  let sortField = defaultSort;
  let sortOrder: 'asc' | 'desc' = 'asc';

  if (sort) {
    const sortSplit = sort.split(',');
    sortField = sortSplit[0] || defaultSort;
    sortOrder = (sortSplit[1] as 'asc' | 'desc') || 'asc';
  }

  return {
    activePage: page ? parseInt(page, 10) : 1,
    itemsPerPage,
    sort: sortField,
    order: sortOrder,
  };
};
