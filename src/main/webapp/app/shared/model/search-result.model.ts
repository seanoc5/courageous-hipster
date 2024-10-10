import dayjs from 'dayjs';
import { ISearchConfiguration } from 'app/shared/model/search-configuration.model';
import { ISearch } from 'app/shared/model/search.model';

export interface ISearchResult {
  id?: number;
  query?: string;
  type?: string | null;
  responseBody?: string | null;
  statusCode?: number | null;
  dateCreated?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  config?: ISearchConfiguration | null;
  search?: ISearch | null;
}

export const defaultValue: Readonly<ISearchResult> = {};
