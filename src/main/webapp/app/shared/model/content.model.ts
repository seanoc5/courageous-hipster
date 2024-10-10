import dayjs from 'dayjs';
import { ISearchResult } from 'app/shared/model/search-result.model';

export interface IContent {
  id?: number;
  title?: string;
  uri?: string;
  description?: string | null;
  path?: string | null;
  source?: string | null;
  params?: string | null;
  bodyText?: string | null;
  textSize?: number | null;
  structuredContent?: string | null;
  structureSize?: number | null;
  author?: string | null;
  language?: string | null;
  type?: string | null;
  subtype?: string | null;
  mineType?: string | null;
  publishDate?: dayjs.Dayjs | null;
  offensiveFlag?: string | null;
  favicon?: string | null;
  dateCreated?: dayjs.Dayjs | null;
  lastUpdate?: dayjs.Dayjs | null;
  searchResult?: ISearchResult | null;
}

export const defaultValue: Readonly<IContent> = {};
