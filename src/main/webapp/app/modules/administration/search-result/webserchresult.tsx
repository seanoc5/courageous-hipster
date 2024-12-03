import React from 'react';
import { Avatar, Box, CircularProgress, Divider, List, ListItem, ListItemAvatar, ListItemText } from '@mui/material';

const WebsearchResults = ({ results, isLoading }) => {
  return (
    <div>
      {isLoading ? (
        <Box sx={{ display: 'flex', justifyContent: 'center' }}>
          <CircularProgress />
        </Box>
      ) : (
        <List>
          {results.length &&
            results.map((result, index) => (
              <React.Fragment key={index}>
                <ListItem alignItems="flex-start">
                  <ListItemAvatar>
                    <Avatar alt={result.title} src={result.favicon} />
                  </ListItemAvatar>
                  <ListItemText
                    primary={
                      <React.Fragment>
                        <div>
                          <span style={{ fontWeight: 'bold' }}>{result.title}</span>
                        </div>
                        <a
                          href={result.uri}
                          target="_blank"
                          rel="noopener noreferrer"
                          style={{ fontSize: '0.85rem', color: 'gray', textDecoration: 'none' }}
                        >
                          {result.uri}
                        </a>
                      </React.Fragment>
                    }
                    secondary={
                      <React.Fragment>
                        <a
                          href={result.uri}
                          target="_blank"
                          rel="noopener noreferrer"
                          style={{ textDecoration: 'none', color: 'blue', fontSize: '1rem' }}
                        >
                          {result.title}
                        </a>
                        <div style={{ color: 'gray', marginTop: '4px' }}>{result.description}</div>
                      </React.Fragment>
                    }
                  />
                </ListItem>
                {index < results.length - 1 && <Divider />}
              </React.Fragment>
            ))}
        </List>
      )}
    </div>
  );
};

export default WebsearchResults;
