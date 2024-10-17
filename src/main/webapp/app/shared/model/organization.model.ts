import dayjs from 'dayjs';

export interface IOrganization {
  id?: number;
  name?: string;
  url?: string | null;
  description?: string | null;
  dateCreated?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IOrganization> = {};
