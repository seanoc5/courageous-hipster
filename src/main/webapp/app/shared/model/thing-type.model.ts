import dayjs from 'dayjs';

export interface IThingType {
  id?: number;
  label?: string;
  parentClass?: string | null;
  descrption?: string | null;
  dateCreated?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IThingType> = {};
