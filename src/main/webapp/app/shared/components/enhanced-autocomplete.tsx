import React from 'react';
import { Autocomplete, TextField } from '@mui/material';

interface Option {
  value: string;
  label: string;
}

interface EnhancedAutocompleteProps {
  value: any;
  onChange: (event: React.ChangeEvent<unknown>, newValue: unknown) => void;
  options: Option[];
  label: string;
  isMulti?: boolean;
  id: string;
  className?: string;
  name: string;
  dataCy: string;
}

const EnhancedAutocomplete: React.FC<EnhancedAutocompleteProps> = ({
  value,
  onChange,
  options,
  label,
  isMulti = false,
  id,
  className,
  name,
  dataCy,
}) => (
  <div className="mb-3">
    <div style={{ paddingBottom: '.5rem' }}>{label}</div>
    <Autocomplete
      multiple={isMulti}
      options={options}
      value={value}
      onChange={onChange}
      getOptionLabel={option => option.label}
      id={id}
      className={className}
      renderInput={params => <TextField {...params} variant="outlined" name={name} data-cy={dataCy} />}
    />
  </div>
);

export default EnhancedAutocomplete;
