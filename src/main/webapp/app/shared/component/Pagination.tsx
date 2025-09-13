/**
 * Custom pagination utilities and components to replace react-jhipster pagination
 */
import React from 'react';
import { Pagination, PaginationItem, PaginationLink } from 'reactstrap';

export interface PaginationState {
  activePage: number;
  itemsPerPage: number;
  sort: string;
  order: 'asc' | 'desc';
}

export const getPaginationState = (location: any, itemsPerPage: number, sortField?: string): PaginationState => {
  const params = new URLSearchParams(location.search);
  const page = parseInt(params.get('page') || '1', 10);
  const sort = params.get('sort') || sortField || 'id';
  const order = params.get('order') === 'desc' ? 'desc' : 'asc';

  return {
    activePage: page,
    itemsPerPage,
    sort,
    order,
  };
};

interface ItemCountProps {
  page: number;
  size: number;
  total: number;
}

export const ItemCount: React.FC<ItemCountProps> = ({ page, size, total }) => {
  const first = Math.max(0, (page - 1) * size) + 1;
  const last = Math.min(page * size, total);

  return (
    <div>
      Showing {first} - {last} of {total} items
    </div>
  );
};

interface CustomPaginationProps {
  activePage: number;
  onSelect: (page: number) => void;
  maxButtons?: number;
  totalItems: number;
  itemsPerPage: number;
}

export const CustomPagination: React.FC<CustomPaginationProps> = ({ activePage, onSelect, maxButtons = 5, totalItems, itemsPerPage }) => {
  const totalPages = Math.ceil(totalItems / itemsPerPage);

  if (totalPages <= 1) {
    return null;
  }

  const items = [];
  const startPage = Math.max(1, activePage - Math.floor(maxButtons / 2));
  const endPage = Math.min(totalPages, startPage + maxButtons - 1);

  // Previous button
  items.push(
    <PaginationItem key="prev" disabled={activePage === 1}>
      <PaginationLink previous onClick={() => onSelect(activePage - 1)} />
    </PaginationItem>,
  );

  // Page numbers
  for (let page = startPage; page <= endPage; page++) {
    items.push(
      <PaginationItem key={page} active={page === activePage}>
        <PaginationLink onClick={() => onSelect(page)}>{page}</PaginationLink>
      </PaginationItem>,
    );
  }

  // Next button
  items.push(
    <PaginationItem key="next" disabled={activePage === totalPages}>
      <PaginationLink next onClick={() => onSelect(activePage + 1)} />
    </PaginationItem>,
  );

  return <Pagination>{items}</Pagination>;
};
